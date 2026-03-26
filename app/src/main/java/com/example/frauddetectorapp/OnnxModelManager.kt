package com.example.frauddetectorapp

import android.content.Context
import android.util.Log
import ai.onnxruntime.*
import java.nio.LongBuffer

object OnnxModelManager {

    // Existing spam model
    private var session: OrtSession? = null
    private var env: OrtEnvironment? = null

    // New Tiny BiLSTM model
    private var tinySession: OrtSession? = null
    private var vocab: Map<String, Int> = emptyMap()

    private val maxLen = 64

    // Tiny BiLSTM label mapping
    private val tinyLabels = mapOf(
        0 to "SAFE",
        1 to "SPAM",
        2 to "PHISHING",
        3 to "OTP_FRAUD",
        4 to "INVESTMENT_SCAM",
        5 to "DELIVERY_SCAM",
        6 to "UTILITY_SCAM"
    )

    fun init(context: Context) {
        Log.e("ONNX_DEBUG", "INIT FUNCTION CALLED")

        try {
            if (env == null) {
                Log.e("ONNX_DEBUG", "Creating Environment")
                env = OrtEnvironment.getEnvironment()
            }

            // Load existing spam model
//            if (session == null) {
//                Log.e("ONNX_DEBUG", "Loading spam_model.onnx")
//                val modelBytes = context.assets.open("spam_model.onnx").readBytes()
//                session = env!!.createSession(modelBytes)
//                Log.e("ONNX_DEBUG", "spam_model loaded")
//            }

            // Load Tiny BiLSTM model directly from bytes (it's only 0.13MB, no need to copy to disk)
            if (tinySession == null) {
                Log.e("ONNX_DEBUG", "Loading fraudshield_tiny_final.onnx")
                val modelBytes = context.assets.open("fraudshield_tiny_final.onnx").readBytes()
                tinySession = env!!.createSession(modelBytes)

                tinySession!!.inputInfo.forEach {
                    Log.e("MODEL_INPUT", it.key)
                }

                Log.e("ONNX_DEBUG", "fraudshield_tiny_final.onnx loaded")
            }

            // Load tiny_vocab.txt (one word per line, index = line number)
            if (vocab.isEmpty()) {
                Log.e("ONNX_DEBUG", "Loading tiny_vocab.txt")
                val map = mutableMapOf<String, Int>()
                context.assets.open("tiny_vocab.txt")
                    .bufferedReader().useLines { lines ->
                        lines.forEachIndexed { idx, line ->
                            map[line.trim()] = idx
                        }
                    }
                vocab = map
                Log.e("ONNX_DEBUG", "vocab loaded: ${vocab.size} tokens")
            }

        } catch (e: Exception) {
            Log.e("ONNX_DEBUG", "INIT ERROR: ${e.message}")
            e.printStackTrace()
        }
    }

    // =========================================================
    // Tiny BiLSTM Tokenizer (max length 64, no CLS/SEP tokens)
    // =========================================================
    private fun tokenize(text: String): LongArray {
        val tokens = text.lowercase()
            .replace(Regex("[^a-z0-9\\s]"), " ")
            .trim()
            .split(Regex("\\s+"))
            .filter { it.isNotEmpty() }

        val ids = LongArray(maxLen) { 0L }
        for (i in 0 until minOf(tokens.size, maxLen)) {
            ids[i] = (vocab[tokens[i]] ?: vocab["<unk>"] ?: 1).toLong()
        }
        return ids
    }

    private fun softmax(logits: FloatArray): FloatArray {
        val max = logits.max()
        val exps = logits.map { Math.exp((it - max).toDouble()).toFloat() }
        val sum = exps.sum()
        return exps.map { it / sum }.toFloatArray()
    }

    // =========================================================
    // Tiny BiLSTM Prediction
    // =========================================================
    private fun predictTinyModel(text: String): Pair<String, Float> {
        return try {
            val inputIds = tokenize(text)

            val inputTensor = OnnxTensor.createTensor(
                env,
                LongBuffer.wrap(inputIds),
                longArrayOf(1, maxLen.toLong())
            )

            val inputs = mapOf("input" to inputTensor)
            val output = tinySession!!.run(inputs)
            val logits = (output[0].value as Array<FloatArray>)[0]
            val probs = softmax(logits)
            val maxIdx = probs.indices.maxByOrNull { probs[it] } ?: 0


            Log.e("ML_DEBUG", "----- MODEL DEBUG -----")
            Log.e("ML_DEBUG", "Logits: ${logits.joinToString(",")}")
            Log.e("ML_DEBUG", "Probs: ${probs.joinToString(",")}")
            Log.e("ML_DEBUG", "Predicted index: $maxIdx")
            Log.e("ML_DEBUG", "Confidence: ${probs[maxIdx] * 100}")
            Log.e("ML_DEBUG", "-----------------------")

            val label = tinyLabels[maxIdx] ?: "UNKNOWN"
            val confidence = probs[maxIdx]

            inputTensor.close()
            output.close()

            Log.e("TINY_MODEL", "Label: $label | Confidence: $confidence")
            Pair(label, confidence)

        } catch (e: Exception) {
            Log.e("TINY_MODEL_ERROR", "Failed: ${e.message}")
            Pair("UNKNOWN", 0.5f)
        }
    }

    // =========================================================
    // Main Predict Function (unchanged interface)
    // =========================================================
    fun predict(
        context: Context,
        sender: String,
        text: String
    ): FraudResult {

        try {
            init(context)

            // 🔹 STEP 1 — Original spam model prediction
            val input = arrayOf(arrayOf(text))
            val tensor = OnnxTensor.createTensor(env, input)
            val result = session!!.run(mapOf("input" to tensor))
            val output = result[0].value as LongArray
            val predictedClass = output[0].toInt()

            val aiLabel = when (predictedClass) {
                0 -> "SAFE"
                1 -> "SPAM"
                2 -> "PHISHING"
                else -> "UNKNOWN"
            }

            // 🔹 STEP 2 — Tiny BiLSTM prediction
            val (tinyLabel, tinyConfidence) = predictTinyModel(text)

            Log.e("MODEL_AI", "Original AI: $aiLabel")
            Log.e("MODEL_TINY", "Tiny BiLSTM: $tinyLabel ($tinyConfidence)")

            // 🔹 STEP 3 — TRAI Analysis
            val (senderVerified, senderRisk) =
                TraiRuleEngine.analyzeSender(sender)

            val (keywordRisk, foundKeywords) =
                TraiRuleEngine.analyzeTelemarketing(text)

            // 🔹 STEP 4 — URL Risk
            var urlRisk = 0
            if (text.contains("http", true) ||
                text.contains(".com", true) ||
                text.contains(".in", true) ||
                text.contains(".net", true)
            ) {
                urlRisk = 2
            }

            val totalRisk = senderRisk + keywordRisk + urlRisk

            // 🔹 STEP 5 — Smart Hybrid Decision
            val finalLabel = when {

                // Both models agree it's phishing → high confidence
                aiLabel == "PHISHING" && tinyLabel == "PHISHING" -> "PHISHING"

                // Tiny model found specific fraud category with high confidence
                tinyLabel == "OTP_FRAUD" && tinyConfidence > 0.75f -> "PHISHING"
                tinyLabel == "INVESTMENT_SCAM" && tinyConfidence > 0.75f -> "SPAM"
                tinyLabel == "DELIVERY_SCAM" && tinyConfidence > 0.75f -> "SPAM"
                tinyLabel == "UTILITY_SCAM" && tinyConfidence > 0.75f -> "SPAM"

                // Original model says phishing
                aiLabel == "PHISHING" -> "PHISHING"

                // Tiny model says phishing with high confidence
                tinyLabel == "PHISHING" && tinyConfidence > 0.80f -> "PHISHING"

                // Both agree spam
                aiLabel == "SPAM" && tinyLabel == "SPAM" -> "SPAM"

                // Original spam + risk score
                aiLabel == "SPAM" && totalRisk >= 2 -> "SPAM"

                // Tiny model spam with high confidence
                tinyLabel == "SPAM" && tinyConfidence > 0.80f -> "SPAM"

                // Escalate safe to suspicious
                aiLabel == "SAFE" && totalRisk >= 3 -> "SUSPICIOUS"

                else -> aiLabel
            }

            // 🔹 STEP 6 — Confidence
            val confidence = when {
                tinyLabel != "UNKNOWN" && tinyConfidence > 0.7f -> tinyConfidence
                finalLabel == "PHISHING" -> 0.95f
                finalLabel == "SUSPICIOUS" -> 0.85f
                finalLabel == "SPAM" -> 0.80f
                finalLabel == "SAFE" -> 0.90f
                else -> 0.5f
            }

            Log.e("MODEL_FINAL", "Final Label: $finalLabel | Confidence: $confidence")

            return FraudResult(
                label = finalLabel,
                confidence = confidence,
                highlightedWords = foundKeywords,
                aiLabel = aiLabel,
                traiRiskScore = totalRisk,
                senderVerified = senderVerified
            )

        } catch (e: Exception) {
            Log.e("ONNX_ERROR", "FAILED: ${e.message}")
            return FraudResult(
                label = "UNKNOWN",
                confidence = 0.5f,
                highlightedWords = emptyList(),
                aiLabel = "UNKNOWN",
                traiRiskScore = 0,
                senderVerified = false
            )
        }
    }
}
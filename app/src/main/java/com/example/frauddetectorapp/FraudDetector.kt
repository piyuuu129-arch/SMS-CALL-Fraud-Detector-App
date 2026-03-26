package com.example.frauddetectorapp

import ai.onnxruntime.*
import android.content.Context
import android.util.Log
import java.nio.LongBuffer

class FraudDetector(context: Context) {

    private val env = OrtEnvironment.getEnvironment()
    private val session: OrtSession
    private val vocab: Map<String, Int>
    private val maxLen = 64

    val labels = mapOf(
        0 to "Safe",
        1 to "Spam",
        2 to "Phishing",
        3 to "OTP Fraud",
        4 to "Investment Scam",
        5 to "Delivery Scam",
        6 to "Utility Scam"
    )

    init {

        // Load ONNX model
        val modelBytes = context.assets.open("fraudshield_tiny_final.onnx").readBytes()
        session = env.createSession(modelBytes)

        // Load vocab
        vocab = loadVocab(context)

        // DEBUG: print model input info
        val inputInfo = session.inputInfo
        for ((name, info) in inputInfo) {
            Log.d("ONNX_DEBUG", "Input name: $name")
            Log.d("ONNX_DEBUG", "Input info: $info")
        }
    }

    private fun loadVocab(context: Context): Map<String, Int> {
        val map = mutableMapOf<String, Int>()

        context.assets.open("tiny_vocab.txt")
            .bufferedReader()
            .useLines { lines ->
                lines.forEachIndexed { index, line ->
                    map[line.trim()] = index
                }
            }

        Log.d("ONNX_DEBUG", "Vocab loaded: ${map.size}")

        return map
    }

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

        Log.d("ONNX_DEBUG", "Tokenized length: ${tokens.size}")

        return ids
    }

    fun analyze(smsText: String): Pair<String, Float> {

        try {

            val inputIds = tokenize(smsText)

            Log.d("ONNX_DEBUG", "First token id: ${inputIds[0]}")

            val inputTensor = OnnxTensor.createTensor(
                env,
                LongBuffer.wrap(inputIds),
                longArrayOf(1, maxLen.toLong())
            )

            val inputs = HashMap<String, OnnxTensor>()
            inputs["input"] = inputTensor

            val results = session.run(inputs)

            val logits = (results[0].value as Array<FloatArray>)[0]

            val probs = softmax(logits)

            val maxIdx = probs.indices.maxByOrNull { probs[it] } ?: 0

            inputTensor.close()
            results.close()

            val label = labels[maxIdx] ?: "Unknown"
            val confidence = probs[maxIdx] * 100

            Log.d("ONNX_DEBUG", "Prediction: $label ($confidence)")

            return Pair(label, confidence)

        } catch (e: Exception) {

            Log.e("ONNX_DEBUG", "ONNX inference failed", e)

            return Pair("Unknown", 0f)
        }
    }

    fun getRiskLevel(category: String, confidence: Float): String {

        if (category == "Safe") return "SAFE"

        return when {
            confidence >= 85f -> "HIGH"
            confidence >= 60f -> "MEDIUM"
            else -> "LOW"
        }
    }

    private fun softmax(logits: FloatArray): FloatArray {

        val max = logits.max()

        val exps = logits.map { Math.exp((it - max).toDouble()).toFloat() }

        val sum = exps.sum()

        return exps.map { it / sum }.toFloatArray()
    }
}
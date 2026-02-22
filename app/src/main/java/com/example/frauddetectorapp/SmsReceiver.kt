package com.example.frauddetectorapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import androidx.core.app.NotificationCompat

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val bundle = intent.extras ?: return
        val pdus = bundle.get("pdus") as? Array<*> ?: return

        var message = ""

        for (pdu in pdus) {
            val format = bundle.getString("format")
            val sms = android.telephony.SmsMessage.createFromPdu(pdu as ByteArray, format)
            message += sms.messageBody
        }

        if (message.isNotEmpty()) {
            analyzeMessage(context, message)
        }
    }

    // =========================================================
    private fun analyzeMessage(context: Context, message: String) {

        Log.e("FLOW", "analyzeMessage running")

        val tfliteResult = runTfliteModel(context, message)
        val onnxResult = OnnxModelManager.predict(context, message)
        val ruleResult = detectByRules(message)

        Log.e("MODEL_CHECK", "TFLITE = $tfliteResult")
        Log.e("MODEL_CHECK", "ONNX = $onnxResult")
        Log.e("MODEL_CHECK", "RULE  = $ruleResult")

        val finalLabel = decideFinalLabel(
            tfliteResult,
            onnxResult,
            ruleResult
        )

        Log.e("FLOW", "FINAL = $finalLabel")

        showNotification(context, "Fraud Shield", "$finalLabel\n$message")

        val tone = mapTone(finalLabel)

        // 🔥 Generate time once
        val time = java.text.SimpleDateFormat(
            "dd MMM yyyy • hh:mm a",
            java.util.Locale.getDefault()
        ).format(java.util.Date())

        // ================= SAVE SMS =================
        HistoryManager.saveSMS(
            context,
            "$message --- $finalLabel --- TONE:$tone"
        )

        // ================= SAVE URL =================
        // ================= SAVE URL =================
        val urls = extractUrls(message)

        Log.e("URL_DEBUG", "Found URLs: $urls")

        urls.forEach { url ->

            // classify URL independently
            val urlLabel = classifyUrl(url)

            Log.e("URL_DEBUG", "Saving URL: $url with label $urlLabel")

            val entry = "$url --- $urlLabel"
            HistoryManager.saveURL(context, entry)
        }
    }

    // =========================================================
    private fun detectByRules(msg: String): String {
        val m = msg.lowercase()

        if (m.contains("bank") || m.contains("otp") || m.contains("verify"))
            return "PHISHING"

        if (m.contains("win") || m.contains("offer") || m.contains("prize") || m.contains("click"))
            return "SPAM"

        return "SAFE"
    }

    private fun mapTone(label: String): String {
        return when (label) {
            "PHISHING" -> "URGENT"
            "SPAM" -> "WARNING"
            "SAFE" -> "NORMAL"
            else -> "NORMAL"
        }
    }

    // =========================================================
    private fun runTfliteModel(context: Context, text: String): String {
        return try {

            val interpreter = ModelManager.getInterpreter(context)
            val vector = TextVectorizer.vectorize(text)

            val input = java.nio.ByteBuffer.allocateDirect(4 * vector.size)
            input.order(java.nio.ByteOrder.nativeOrder())

            for (v in vector) input.putFloat(v)

            val output = Array(1) { FloatArray(4) }
            interpreter?.run(input, output)

            val scores = output[0]
            Log.e("TFLITE_RAW", scores.joinToString())

            var maxIndex = 0
            var maxVal = scores[0]

            for (i in scores.indices) {
                if (scores[i] > maxVal) {
                    maxVal = scores[i]
                    maxIndex = i
                }
            }

            if (maxVal < 0.05f) return "UNKNOWN"

            return when (maxIndex) {
                0 -> "SAFE"
                1 -> "SPAM"
                2 -> "PHISHING"
                else -> "UNKNOWN"
            }

        } catch (e: Exception) {
            Log.e("ML", e.toString())
            return "UNKNOWN"
        }
    }

    private fun runOnnxModel(context: Context, text: String): String {
        return try {
            OnnxModelManager.predict(context, text)
        } catch (e: Exception) {
            Log.e("ONNX_ERROR", e.message ?: "")
            return "UNKNOWN"
        }
    }

    private fun decideFinalLabel(
        tflite: String,
        onnx: String,
        rule: String
    ): String {

        if (onnx == "PHISHING") return "PHISHING"
        if (tflite == "PHISHING") return "PHISHING"

        if (onnx == "SPAM") return "SPAM"
        if (tflite == "SPAM") return "SPAM"

        if (rule == "PHISHING") return "PHISHING"
        if (rule == "SPAM") return "SPAM"

        return "SAFE"
    }

    // =========================================================
    private fun extractUrls(message: String): List<String> {

        val regex = Regex(
            """\b((https?://|www\.)?[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}(/[^\s]*)?)"""
        )

        return regex.findAll(message)
            .map { it.value }
            .filter { it.contains(".") }
            .toList()
    }


    private fun classifyUrl(url: String): String {

        val u = url.lowercase()

        // ---------- PHISHING ----------
        val phishingKeywords = listOf(
            "bank", "login", "signin", "verify", "update",
            "secure", "account", "wallet", "upi", "kbank",
            "sbi", "hdfc", "icici", "axis", "netbank",
            "otp", "password", "reset", "card", "kyc",
            "alert", "suspend", "blocked"
        )

        if (phishingKeywords.any { u.contains(it) }) {
            return "PHISHING"
        }

        // ---------- SPAM ----------
        val spamKeywords = listOf(
            "win", "offer", "free", "bonus", "prize",
            "reward", "cash", "gift", "deal", "promo",
            "click", "subscribe", "limited", "urgent",
            "sale", "discount", "lottery"
        )

        if (spamKeywords.any { u.contains(it) }) {
            return "SPAM"
        }

        // ---------- SAFE ----------
        return "SAFE"
    }


    // =========================================================
    private fun showNotification(context: Context, title: String, msg: String) {

        val channelId = "fraud_alerts"

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Fraud Alerts",
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(msg.take(80))
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
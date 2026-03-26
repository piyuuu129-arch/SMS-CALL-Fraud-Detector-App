package com.example.frauddetectorapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import androidx.core.app.NotificationCompat

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Log.d("SMS_DEBUG", "🔥 SmsReceiver triggered!")

        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return

        val bundle = intent.extras ?: return
        val pdus = bundle.get("pdus") as? Array<*> ?: return

        var message = ""
        var sender = "UNKNOWN"

        for (pdu in pdus) {
            val format = bundle.getString("format")
            val sms = android.telephony.SmsMessage
                .createFromPdu(pdu as ByteArray, format)

            sender = sms.originatingAddress ?: "UNKNOWN"
            message += sms.messageBody
        }

        if (message.isNotEmpty()) {
            analyzeMessage(context, sender, message)
        }
    }

    // =========================================================
    private fun analyzeMessage(
        context: Context,
        sender: String,
        message: String
    ) {

        Log.e("FLOW", "analyzeMessage running")

        val lowerMsg = message.lowercase()

        val wordCount = message.trim().split("\\s+".toRegex()).size

        val smallTalkWords = listOf(
            "hi","hello","hey","hii","hiii","ok","okay","thanks","thank you","bro","yo"
        )

        if(wordCount <= 3 && smallTalkWords.any { lowerMsg.contains(it) }){

            val finalPublicLabel = "SAFE"

            showNotification(
                context,
                finalPublicLabel,
                message,
                100f,
                "None",
                0,
                true
            )

            HistoryManager.saveSMS(
                context,
                "$sender || $message --- SAFE --- CONF:100 --- TONE:NORMAL --- KEYWORDS:None --- TRAI:0 --- VERIFIED:true"
            )

            return
        }

        val investmentKeywords = listOf(
            "invest",
            "investment",
            "trading",
            "crypto",
            "profit",
            "returns",
            "double money",
            "earn daily",
            "guaranteed income",
            "trading scheme"
        )

        if(investmentKeywords.any { lowerMsg.contains(it) }){

            val finalPublicLabel = "INVESTMENT SCAM"

            showNotification(
                context,
                finalPublicLabel,
                message,
                95f,
                "investment",
                90,
                false
            )

            HistoryManager.saveSMS(
                context,
                "$sender || $message --- INVESTMENT SCAM --- CONF:95 --- TONE:WARNING --- KEYWORDS:investment --- TRAI:90 --- VERIFIED:false"
            )

            return
        }


        val phishingKeywords = listOf(
            "click here",
            "claim prize",
            "verify account",
            "login now",
            "update account",
            "free iphone",
            "win prize",
            "verify details"
        )

        if(phishingKeywords.any { lowerMsg.contains(it) }){

            val finalPublicLabel = "PHISHING"

            showNotification(
                context,
                finalPublicLabel,
                message,
                98f,
                "phishing",
                95,
                false
            )

            HistoryManager.saveSMS(
                context,
                "$sender || $message --- PHISHING --- CONF:98 --- TONE:URGENT --- KEYWORDS:phishing --- TRAI:95 --- VERIFIED:false"
            )

            return
        }

        // ✅ Use working FraudDetector instead of broken OnnxModelManager
        val detector = FraudDetector(context)
        val prediction = detector.analyze(message)

        val engineLabel = prediction.first.uppercase()
        val confidence = prediction.second

        // Dummy values to keep rest of pipeline intact
        val keywordsString = "None"
        val totalRisk = confidence.toInt()
        val senderVerified = false

        // 2️⃣ Extract URLs
        val urls = extractUrls(message)
        var forcePhishing = false

        urls.forEach { url ->
            val urlLabel = classifyUrl(url)

            if (urlLabel == "PHISHING") {
                forcePhishing = true
            }

            HistoryManager.saveURL(context, "$url --- $urlLabel")
        }

        // 3️⃣ Final Public Label
        val finalPublicLabel = when {
            forcePhishing -> "PHISHING"
            engineLabel == "SPAM" -> "SPAM"
            else -> engineLabel
        }

        Log.e("FINAL_LABEL", "Engine: $engineLabel | Public: $finalPublicLabel")

        // 4️⃣ Tone
        val tone = mapTone(finalPublicLabel)

        // 5️⃣ Sender Name
        val finalSender = if (sender.contains("-")) {
            sender
        } else {
            ContactHelper.getContactName(context, sender) ?: sender
        }

        // 7️⃣ Show Notification
        showNotification(
            context,
            finalPublicLabel,
            message,
            confidence,
            keywordsString,
            totalRisk,
            senderVerified
        )

        // 8️⃣ Save SMS
        HistoryManager.saveSMS(
            context,
            "$finalSender || $message --- $finalPublicLabel " +
                    "--- CONF:$confidence " +
                    "--- TONE:$tone " +
                    "--- KEYWORDS:$keywordsString " +
                    "--- TRAI:$totalRisk " +
                    "--- VERIFIED:$senderVerified"
        )

        // =========================================================
        // 9️⃣ IMPERSONATION FRAUD DETECTION

        val isSavedContact =
            ContactHelper.getContactName(context, sender) != null

        val financialKeywords = listOf(
            "send money",
            "urgent money",
            "transfer money",
            "need money",
            "upi",
            "bank transfer",
            "emergency",
            "please send",
            "help me with money"
        )

        val financialRequest =
            financialKeywords.any { message.lowercase().contains(it) }

        if (financialRequest && isSavedContact) {

            val warning = """
⚠ Possible Fraud

Sender: $finalSender

This message requests urgent money.

Recommendation:
Call the sender directly to verify identity.
""".trimIndent()

            showSimpleAlert(
                context,
                "Fraud Shield Warning",
                warning
            )
        }
    }

    // =========================================================
    private fun showNotification(
        context: Context,
        label: String,
        message: String,
        confidence: Float,
        keywords: String,
        traiRisk: Int,
        senderVerified: Boolean
    ) {

        val channelId = "fraud_alerts"

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channel = NotificationChannel(
            channelId,
            "Fraud Alerts",
            NotificationManager.IMPORTANCE_HIGH
        )
        manager.createNotificationChannel(channel)

        val intent = Intent(context, FraudAlertActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("label", label)
            putExtra("confidence", confidence)
            putExtra("message", message)
            putExtra("keywords", keywords)
            putExtra("traiRisk", traiRisk)
            putExtra("senderVerified", senderVerified)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Fraud Shield")
            .setContentText("$label\n${message.take(60)}")
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    // =========================================================
    private fun showSimpleAlert(
        context: Context,
        title: String,
        message: String
    ) {

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(context, "fraud_alerts")
            .setContentTitle(title)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setSmallIcon(android.R.drawable.stat_notify_error)
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setAutoCancel(true)
            .build()

        manager.notify(System.currentTimeMillis().toInt(), notification)
    }

    // =========================================================
    private fun mapTone(label: String): String {
        return when (label) {
            "PHISHING" -> "URGENT"
            "SPAM" -> "WARNING"
            else -> "NORMAL"
        }
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

    // =========================================================
    private fun classifyUrl(url: String): String {

        val u = url.lowercase()

        val phishingKeywords = listOf(
            "bank","login","signin","verify","update",
            "secure","account","wallet","upi",
            "sbi","hdfc","icici","axis",
            "otp","password","reset","kyc",
            "alert","suspend","blocked"
        )

        if (phishingKeywords.any { u.contains(it) }) {
            return "PHISHING"
        }

        val spamKeywords = listOf(
            "win","offer","free","bonus","prize",
            "reward","cash","gift","deal",
            "click","subscribe","limited",
            "sale","discount","lottery"
        )

        if (spamKeywords.any { u.contains(it) }) {
            return "SPAM"
        }

        return "SAFE"
    }
}
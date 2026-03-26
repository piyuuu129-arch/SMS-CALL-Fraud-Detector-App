package com.example.frauddetectorapp

import android.graphics.Color
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FraudAlertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fraud_alert)

        // 🔥 Read Intent Data
        val keywords = intent.getStringExtra("keywords") ?: "None"
        val label = intent.getStringExtra("label") ?: "UNKNOWN"
        val message = intent.getStringExtra("message") ?: ""
        val confidence = intent.getFloatExtra("confidence", 0f)
        val traiRisk = intent.getIntExtra("traiRisk", 0)
        val senderVerified = intent.getBooleanExtra("senderVerified", false)

        val root = findViewById<LinearLayout>(R.id.rootLayout)
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvMsg = findViewById<TextView>(R.id.tvMessage)
        val tvConf = findViewById<TextView>(R.id.tvConfidence)
        val tvWords = findViewById<TextView>(R.id.tvWords)
        val tvTraiRisk = findViewById<TextView>(R.id.tvTraiRisk)
        val tvSenderStatus = findViewById<TextView>(R.id.tvSenderStatus)

        // 🔥 Dynamic Risk Level
        val riskLevel = when {
            traiRisk <= 1 -> "LOW"
            traiRisk <= 3 -> "MEDIUM"
            else -> "HIGH"
        }

        tvTraiRisk.text = "TRAI Risk Score: $traiRisk ($riskLevel)"

        if (senderVerified) {
            tvSenderStatus.text = "✔ Verified Sender"
            tvSenderStatus.setTextColor(Color.GREEN)
        } else {
            tvSenderStatus.text = "⚠ Unregistered Sender"
            tvSenderStatus.setTextColor(Color.RED)
        }

        // 🔥 Background Based on Label
        when (label) {

            "SAFE" -> {
                root.setBackgroundColor(Color.parseColor("#003300"))
                tvTitle.text = "SAFE MESSAGE"
                tvTitle.setTextColor(Color.GREEN)
            }

            "SPAM" -> {
                root.setBackgroundColor(Color.parseColor("#332200"))
                tvTitle.text = "SPAM DETECTED"
                tvTitle.setTextColor(Color.parseColor("#FFA500"))
            }

            "PHISHING" -> {
                root.setBackgroundColor(Color.parseColor("#330000"))
                tvTitle.text = "PHISHING ALERT"
                tvTitle.setTextColor(Color.RED)
            }

            "SUSPICIOUS" -> {
                root.setBackgroundColor(Color.parseColor("#332200"))
                tvTitle.text = "SUSPICIOUS MESSAGE"
                tvTitle.setTextColor(Color.parseColor("#FFCC00"))
            }

            else -> {
                root.setBackgroundColor(Color.BLACK)
                tvTitle.text = "UNKNOWN STATUS"
                tvTitle.setTextColor(Color.WHITE)
            }
        }

        // 🔥 Display Content
        tvMsg.text = message
        val confidencePercent = if (confidence <= 1f) confidence * 100 else confidence
        tvConf.text = "Confidence: ${confidencePercent.toInt().coerceIn(0, 100)}%"

        // ✅ FIXED — Display Real Keywords
        tvWords.text = "Suspicious Keywords: $keywords"
    }
}
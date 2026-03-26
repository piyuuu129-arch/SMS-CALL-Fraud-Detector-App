package com.example.frauddetectorapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class SmsStatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ FIRST set UI
        setContentView(R.layout.activity_sms_stats)

        // ✅ FIND VIEWS
        val safeText = findViewById<TextView>(R.id.safeCount)
        val spamText = findViewById<TextView>(R.id.spamCount)
        val scamText = findViewById<TextView>(R.id.scamCount)
        val phishingText = findViewById<TextView>(R.id.phishingCount)

        // ✅ GET SMS HISTORY
        val smsLogs = HistoryManager.getSmsLogs(this)

        var safe = 0
        var spam = 0
        var scam = 0
        var phishing = 0

        // ✅ COUNT FROM HISTORY
        for (sms in smsLogs) {

            val text = sms.lowercase()

            when {
                text.contains("safe") -> safe++
                text.contains("spam") -> spam++
                text.contains("scam") -> scam++
                text.contains("phishing") -> phishing++
            }
        }

        // ✅ SET REAL VALUES
        safeText.text = safe.toString()
        spamText.text = spam.toString()
        scamText.text = scam.toString()
        phishingText.text = phishing.toString()
    }
}
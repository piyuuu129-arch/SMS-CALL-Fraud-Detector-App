package com.example.frauddetectorapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView

class CallStatsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Load UI first
        setContentView(R.layout.activity_call_stats)

        // ✅ Find views
        val safeText = findViewById<TextView>(R.id.safeCount)
        val fraudText = findViewById<TextView>(R.id.fraudCount)
        val unknownText = findViewById<TextView>(R.id.unknownCount)

        // ✅ Get call logs
        val callLogs = HistoryManager.getCallLogs(this)

        var safe = 0
        var fraud = 0
        var unknown = 0

        // ✅ Count from history
        for (call in callLogs) {

            val text = call.lowercase()

            when {
                text.contains("safe", ignoreCase = true) -> safe++

                text.contains("fraud", ignoreCase = true) ||
                        text.contains("scam", ignoreCase = true) -> fraud++

                text.contains("unknown", ignoreCase = true) -> unknown++

                else -> unknown++
            }
        }

        // ✅ Set values
        safeText.text = safe.toString()
        fraudText.text = fraud.toString()
        unknownText.text = unknown.toString()
    }
}
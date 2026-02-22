package com.example.frauddetectorapp

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class UrlHistoryActivity : AppCompatActivity() {

    private lateinit var container: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_url_history)

        container = findViewById(R.id.historyContainer)
        val clearBtn = findViewById<Button>(R.id.clearUrlBtn)

        loadHistory()

        clearBtn.setOnClickListener {
            HistoryManager.clearUrlLogs(this)
            container.removeAllViews()
            loadHistory()
        }
    }

    override fun onResume() {
        super.onResume()
        container.removeAllViews()
        loadHistory()
    }

    private fun loadHistory() {

        val logs = HistoryManager.getUrlLogs(this)
        if (logs.isEmpty()) return

        logs.forEach { entry ->

            val card = CardView(this)
            card.radius = 30f
            card.cardElevation = 12f
            card.setCardBackgroundColor(Color.parseColor("#242424"))
            card.useCompatPadding = true
            card.setContentPadding(35, 30, 35, 30)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 28)
            card.layoutParams = params

            val layout = LinearLayout(this)
            layout.orientation = LinearLayout.VERTICAL

            // Detect label
            val label = when {
                entry.contains("PHISHING") -> "PHISHING"
                entry.contains("SPAM") -> "SPAM"
                else -> "SAFE"
            }

            val text = TextView(this)
            text.textSize = 15f

            when (label) {
                "PHISHING" -> {
                    text.text = "🚨 Phishing URL\n$entry"
                    text.setTextColor(Color.RED)
                }

                "SPAM" -> {
                    text.text = "⚠ Spam URL\n$entry"
                    text.setTextColor(Color.parseColor("#FFA500"))
                }

                else -> {
                    text.text = "✔ Safe URL\n$entry"
                    text.setTextColor(Color.parseColor("#00FF7F"))
                }
            }

            layout.addView(text)
            card.addView(layout)
            container.addView(card)
        }
    }
}
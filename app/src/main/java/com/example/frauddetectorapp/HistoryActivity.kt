package com.example.frauddetectorapp

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_history)


        val clearBtn = findViewById<Button>(R.id.clearCallBtn)



        clearBtn.setOnClickListener {
            HistoryManager.clearCallLogs(this)

        }
    }

    private fun loadHistory(container: LinearLayout) {

        val logs = HistoryManager.getCallLogs(this)

        if (logs.isEmpty()) return

        logs.forEach { entry ->

            val card = CardView(this)

            card.cardElevation = 12f
            card.radius = 28f
            card.setContentPadding(25,25,25,25)

            val text = TextView(this)
            text.text = entry
            text.setTextColor(Color.WHITE)
            text.textSize = 14f

            if (entry.contains("Fraud")) {
                card.setCardBackgroundColor(Color.parseColor("#2B0B0B"))  // deep dark red
            } else {
                card.setCardBackgroundColor(Color.parseColor("#0F2E17"))  // deep dark green
            }

            card.addView(text)

            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(0, 0, 0, 30)
            card.layoutParams = params

            container.addView(card)
        }
    }
}
package com.example.frauddetectorapp

import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.card.MaterialCardView


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

            val card = MaterialCardView(this)



            card.radius = 20f
            card.cardElevation = 18f
            card.setCardBackgroundColor(Color.parseColor("#1E1E1E"))
            card.setContentPadding(20,20,20,20)

            card.strokeWidth = 4




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

            // Badge
            val badge = TextView(this)
            badge.textSize = 12f
            badge.setPadding(32,12,32,12)
            badge.background = getDrawable(R.drawable.badge_background)





            badge.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )


            // Time / URL header
            val time = TextView(this)
            time.textSize = 12f
            time.setPadding(0,8,0,0)
            time.setTextColor(Color.parseColor("#9A9A9A"))



            // URL text
            val urlText = TextView(this)
            urlText.textSize = 15f
            urlText.setPadding(0,12,0,0)

            when(label){

                "PHISHING" ->{

                    badge.text = "🚨 PHISHING URL"
                    badge.setTextColor(Color.WHITE)
                    val bg = getDrawable(R.drawable.badge_background)?.mutate()
                    bg?.setTint(Color.parseColor("#E53935"))
                    badge.background = bg


                    urlText.setTextColor(Color.WHITE)



                    card.strokeColor = Color.parseColor("#E53935")
                    card.cardElevation = 18f

                }

                "SPAM" ->{

                    badge.text = "⚠ SPAM URL"
                    badge.setTextColor(Color.WHITE)

                    val bg = getDrawable(R.drawable.badge_background)?.mutate()
                    bg?.setTint(Color.parseColor("#FFA000"))
                    badge.background = bg

                    urlText.setTextColor(Color.WHITE)

                    card.strokeColor = Color.parseColor("#FFA000")
                    card.cardElevation = 18f

                }


                else ->{

                    badge.text = "✔ SAFE URL"
                    badge.setTextColor(Color.WHITE)

                    val bg = getDrawable(R.drawable.badge_background)?.mutate()
                    bg?.setTint(Color.parseColor("#2E7D32"))
                    badge.background = bg

                    urlText.setTextColor(Color.WHITE)

                    card.strokeColor = Color.parseColor("#2E7D32")
                    card.cardElevation = 18f

                }

            }

            time.text = "🌐 " + entry.substringBefore("]") + "]"


            urlText.text = entry.substringBefore(" ---").substringAfter("]")



            layout.addView(badge)
            layout.addView(time)
            layout.addView(urlText)

            card.addView(layout)
            container.addView(card)
        }
    }
}

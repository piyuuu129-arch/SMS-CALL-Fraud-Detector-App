package com.example.frauddetectorapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SmsHistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_history)

        val recycler = findViewById<RecyclerView>(R.id.recyclerSms)
        val clearBtn = findViewById<Button>(R.id.clearSmsBtn)

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = SmsAdapter(HistoryManager.getSmsLogs(this))

        clearBtn.setOnClickListener {
            HistoryManager.clearSmsLogs(this)
            recycler.adapter = SmsAdapter(emptyList())
        }
    }
}
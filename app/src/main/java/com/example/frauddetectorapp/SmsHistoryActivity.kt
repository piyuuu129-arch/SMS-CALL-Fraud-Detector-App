package com.example.frauddetectorapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.widget.Button

class SmsHistoryActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var adapter: SmsAdapter
    private lateinit var smsList: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sms_history)

        recycler = findViewById(R.id.recyclerSms)
        val clearBtn = findViewById<Button>(R.id.clearSmsBtn)

        recycler.layoutManager = LinearLayoutManager(this)

        // ✅ Convert to MutableList (VERY IMPORTANT)
        smsList = HistoryManager.getSmsLogs(this).toMutableList()

        adapter = SmsAdapter(smsList) {
                message, label, confidence, keywords, traiRisk, senderVerified ->

            val intent = Intent(this, FraudAlertActivity::class.java)

            val sender = "UNKNOWN"
            val (senderVerified, traiRisk) =
                TraiRuleEngine.analyzeSender(sender)

            intent.putExtra("label", label)
            intent.putExtra("message", message)
            intent.putExtra("confidence", confidence)
            intent.putExtra("keywords", keywords)          // ✅ ADD THIS
            intent.putExtra("traiRisk", traiRisk)
            intent.putExtra("senderVerified", senderVerified)

            startActivity(intent)
        }

        recycler.adapter = adapter

        // ✅ Attach Swipe-to-Delete
        val itemTouchHelper = ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {

                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(
                    viewHolder: RecyclerView.ViewHolder,
                    direction: Int
                ) {
                    val position = viewHolder.adapterPosition

                    // 1️⃣ Delete from SharedPreferences
                    HistoryManager.deleteSmsAt(
                        this@SmsHistoryActivity,
                        position
                    )

                    // 2️⃣ Remove from in-memory list
                    smsList.removeAt(position)

                    // 3️⃣ Notify adapter
                    adapter.notifyItemRemoved(position)
                }
            }
        )

        itemTouchHelper.attachToRecyclerView(recycler)

        clearBtn.setOnClickListener {
            HistoryManager.clearSmsLogs(this)
            smsList.clear()
            adapter.notifyDataSetChanged()
        }
    }
}
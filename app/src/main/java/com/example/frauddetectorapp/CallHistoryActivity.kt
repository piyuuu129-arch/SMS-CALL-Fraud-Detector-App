package com.example.frauddetectorapp

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CallHistoryActivity : AppCompatActivity() {

    private lateinit var adapter: CallAdapter
    private lateinit var list: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call_history)

        val recycler = findViewById<RecyclerView>(R.id.recycler)
        val clearBtn = findViewById<Button>(R.id.clearCallBtn)

        // ✅ NOW getCallLogs already returns List<String>
        list = HistoryManager.getCallLogs(this).toMutableList()

        adapter = CallAdapter(list) { saveLogs() }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        // Swipe delete
        val swipe = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(r: RecyclerView, v: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false

            override fun onSwiped(vh: RecyclerView.ViewHolder, dir: Int) {
                adapter.deleteItem(vh.adapterPosition)
            }
        }

        ItemTouchHelper(swipe).attachToRecyclerView(recycler)

        clearBtn.setOnClickListener {
            list.clear()
            adapter.notifyDataSetChanged()
            saveLogs()
        }
    }

    private fun saveLogs() {
        // save updated list back to history
        val prefs = getSharedPreferences("history", MODE_PRIVATE)
        val text = list.joinToString("\n\n")
        prefs.edit().putString("call_logs", text).apply()
    }

    override fun onResume() {
        super.onResume()
        list.clear()
        list.addAll(HistoryManager.getCallLogs(this))
        adapter.notifyDataSetChanged()
    }
}
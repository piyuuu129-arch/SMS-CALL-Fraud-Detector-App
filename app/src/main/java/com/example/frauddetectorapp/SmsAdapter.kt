package com.example.frauddetectorapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frauddetectorapp.databinding.ItemCallBinding

class SmsAdapter(private val list: List<String>) :
    RecyclerView.Adapter<SmsAdapter.VH>() {

    inner class VH(val bind: ItemCallBinding) :
        RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCallBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VH(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {

        val entry = list[position]

        holder.bind.dateText.text = ""
        holder.bind.callText.text = "💬 $entry"

        val label = when {
            entry.contains("PHISHING") -> "PHISHING"
            entry.contains("SPAM") -> "SPAM"
            else -> "SAFE"
        }

        when (label) {
            "PHISHING" -> {
                holder.bind.resultText.text = "🚨 Phishing SMS"
                holder.bind.resultText.setTextColor(Color.RED)
            }

            "SPAM" -> {
                holder.bind.resultText.text = "⚠ Spam SMS"
                holder.bind.resultText.setTextColor(Color.parseColor("#FFA500"))
            }

            else -> {
                holder.bind.resultText.text = "✔ Safe SMS"
                holder.bind.resultText.setTextColor(Color.GREEN)
            }
        }
    }
}
package com.example.frauddetectorapp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frauddetectorapp.databinding.ItemCallBinding

class CallAdapter(
    private val list: MutableList<String>,
    private val onDelete: (Int) -> Unit
) : RecyclerView.Adapter<CallAdapter.VH>() {

    inner class VH(val bind: ItemCallBinding) : RecyclerView.ViewHolder(bind.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemCallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: VH, position: Int) {

        val entry = list[position]

        holder.bind.dateText.text = entry.substringBefore("\n")

        val callLine = entry.substringAfter("\n").substringBefore("\n")
        holder.bind.callText.text = "📞 $callLine"

        if (entry.contains("Fraud")) {
            holder.bind.resultText.text = "⚠ Fraud Call Detected"
            holder.bind.resultText.setTextColor(Color.RED)
        } else {
            holder.bind.resultText.text = "✔ Safe Call"
            holder.bind.resultText.setTextColor(Color.GREEN)
        }
    }

    fun deleteItem(pos: Int) {
        list.removeAt(pos)
        notifyItemRemoved(pos)
        onDelete(pos)
    }
}
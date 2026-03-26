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
        val number = callLine.replace("Call from ", "")
        holder.bind.callText.text = "📞 $number"

        val badge = holder.bind.statusBadge

        // Existing Fraud / Safe detection
        if (entry.contains("Fraud")) {
            badge.text = "⚠ FRAUD CALL"
            badge.setBackgroundResource(R.drawable.badge_phishing)
        } else {
            badge.text = "✔ TRUSTED CALL"
            badge.setBackgroundResource(R.drawable.badge_safe)
        }

        if (entry.contains("Risk:")) {
            val risk = entry.substringAfter("Risk:")
            holder.bind.callText.append("\nRisk Level: $risk")
        }

        if (entry.contains("RECOMMEND_BLOCK:true")) {

            holder.bind.callText.append("\n⚠ Recommended Action: Block Number")

            holder.bind.blockButton.visibility = android.view.View.VISIBLE

            val number = entry.substringAfter("Call from ")
                .substringBefore(" ")

            holder.bind.blockButton.setOnClickListener {
                BlockedNumbersManager.add(number)
                holder.bind.blockButton.text = "Blocked"
                holder.bind.blockButton.isEnabled = false
            }

        } else {

            holder.bind.blockButton.visibility = android.view.View.GONE
        }

        // 🔥 NEW: Block Recommendation Logic
        if (entry.contains("RECOMMEND_BLOCK:true")) {

            holder.bind.callText.append("\n⚠ Recommended Action: Block Number")
        }
    }

    fun deleteItem(pos: Int) {
        list.removeAt(pos)
        notifyItemRemoved(pos)
        onDelete(pos)
    }


}

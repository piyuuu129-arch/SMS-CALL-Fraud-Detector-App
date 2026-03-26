package com.example.frauddetectorapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frauddetectorapp.databinding.ItemCallBinding

class SmsAdapter(
    private val list: List<String>,
    private val onClick: (
        String,
        String,
        Float,
        String,
        Int,
        Boolean
    ) -> Unit
) : RecyclerView.Adapter<SmsAdapter.VH>() {


    inner class VH(val bind: ItemCallBinding) :
        RecyclerView.ViewHolder(bind.root) {

        init {
            bind.root.setOnClickListener {

                val position = bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return@setOnClickListener

                val entry = list[position]

                val sections = entry.split(" --- ")

                val messagePart = sections.getOrNull(0) ?: ""
                val label = sections.getOrNull(1)?.trim() ?: "SAFE"

                val confidence = Regex("CONF:([0-9.]+)")
                    .find(entry)
                    ?.groupValues?.get(1)
                    ?.toFloatOrNull() ?: 0f

                val keywords = Regex("KEYWORDS:([A-Za-z0-9,]*)")
                    .find(entry)
                    ?.groupValues?.get(1)
                    ?: "None"

                val traiRisk = Regex("TRAI:([0-9]+)")
                    .find(entry)
                    ?.groupValues?.get(1)
                    ?.toIntOrNull() ?: 0

                val senderVerified = Regex("VERIFIED:(true|false)")
                    .find(entry)
                    ?.groupValues?.get(1)
                    ?.toBoolean() ?: false

                onClick(
                    messagePart,
                    label,
                    confidence,
                    keywords,
                    traiRisk,
                    senderVerified
                )
            }
        }
    }

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

        val sender = entry.substringBefore("||").trim()

        val message = entry
            .substringAfter("||")
            .substringBefore(" ---")
            .trim()

        holder.bind.dateText.text = "👤 $sender"
        holder.bind.callText.text = "💬 $message"

        val sections = entry.split(" --- ")
        val label = sections.getOrNull(1)?.trim() ?: "SAFE"




        when(label){

            "SAFE" -> {
                holder.bind.cardGlow.setBackgroundResource(R.drawable.glow_safe)
            }

            "SPAM" -> {
                holder.bind.cardGlow.setBackgroundResource(R.drawable.glow_spam)
            }

            "PHISHING" -> {
                holder.bind.cardGlow.setBackgroundResource(R.drawable.glow_phishing)
            }

            "UTILITY SCAM",
            "DELIVERY SCAM",
            "INVESTMENT SCAM",
            "OTP FRAUD" -> {
                holder.bind.cardGlow.setBackgroundResource(R.drawable.glow_scam)
            }
        }

        val badge = holder.bind.statusBadge

        when (label) {

            "SAFE" -> {
                badge.text = "✔ SAFE SMS"
                badge.setBackgroundResource(R.drawable.badge_safe)
            }

            "SPAM" -> {
                badge.text = "⚠ SPAM SMS"
                badge.setBackgroundResource(R.drawable.badge_spam)
            }

            "PHISHING" -> {
                badge.text = "🚨 PHISHING SMS"
                badge.setBackgroundResource(R.drawable.badge_phishing)
            }

            "UTILITY SCAM",
            "DELIVERY SCAM",
            "INVESTMENT SCAM",
            "OTP FRAUD" -> {

                val subCategory = when(label){
                    "UTILITY SCAM" -> "Utility"
                    "DELIVERY SCAM" -> "Delivery"
                    "INVESTMENT SCAM" -> "Investment"
                    "OTP FRAUD" -> "OTP Fraud"
                    else -> "Scam"
                }

                badge.text = "🎭 SCAM SMS • $subCategory"
                badge.setBackgroundResource(R.drawable.badge_scam)
            }

            else -> {
                badge.text = "⚠ SUSPICIOUS"
                badge.setBackgroundResource(R.drawable.badge_spam)
            }
        }
    }


}

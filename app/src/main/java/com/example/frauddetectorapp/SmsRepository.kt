package com.example.frauddetectorapp

import SmsItem

object SmsRepository {

    val smsList = mutableListOf<SmsItem>()

    fun addSms(message: String, label: String) {
        smsList.add(SmsItem(message, label))
    }

    fun getCounts(): Map<String, Int> {
        val counts = mutableMapOf(
            "safe" to 0,
            "spam" to 0,
            "scam" to 0,
            "phishing" to 0
        )

        for (sms in smsList) {
            val key = sms.label.lowercase()
            if (counts.containsKey(key)) {
                counts[key] = counts[key]!! + 1
            }
        }

        return counts
    }
}
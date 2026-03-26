package com.example.frauddetectorapp

import android.content.Context
import java.text.SimpleDateFormat
import java.util.*

class HistoryManager {

    companion object {

        private fun getTime(): String {
            val sdf = SimpleDateFormat("dd MMM yyyy • hh:mm a", Locale.getDefault())
            return sdf.format(Date())
        }

        // ================= CALL =================
        fun saveCall(context: Context, text: String) {
            val prefs = context.getSharedPreferences("history", Context.MODE_PRIVATE)
            val old = prefs.getString("call_logs", "") ?: ""
            val newLog = "[${getTime()}]\n$text\n\n$old"
            prefs.edit().putString("call_logs", newLog).apply()
        }

        fun getCallLogs(context: Context): List<String> {
            val prefs = context.getSharedPreferences("history", Context.MODE_PRIVATE)
            val data = prefs.getString("call_logs", "") ?: ""
            return data.split("\n\n").filter { it.isNotBlank() }
        }

        fun clearCallLogs(context: Context) {
            context.getSharedPreferences("history", Context.MODE_PRIVATE)
                .edit().remove("call_logs").apply()
        }

        // ================= SMS =================
        fun saveSMS(context: Context, text: String) {
            val prefs = context.getSharedPreferences("history", Context.MODE_PRIVATE)
            val old = prefs.getString("sms_logs", "") ?: ""

            val time = SimpleDateFormat(
                "dd MMM yyyy • hh:mm a",
                Locale.getDefault()
            ).format(Date())

            val newLog = "[$time]\n$text\n\n$old"
            prefs.edit().putString("sms_logs", newLog).apply()
        }

        fun getSmsLogs(context: Context): List<String> {
            val prefs = context.getSharedPreferences("history", Context.MODE_PRIVATE)
            val data = prefs.getString("sms_logs", "") ?: ""
            return data.split("\n\n").filter { it.isNotBlank() }
        }

        // ✅ NEW: Delete individual SMS (Swipe support)
        fun deleteSmsAt(context: Context, position: Int) {
            val prefs = context.getSharedPreferences("history", Context.MODE_PRIVATE)
            val data = prefs.getString("sms_logs", "") ?: ""

            val list = data.split("\n\n")
                .filter { it.isNotBlank() }
                .toMutableList()

            if (position >= 0 && position < list.size) {

                list.removeAt(position)

                // Rebuild string in SAME format
                val updated = if (list.isEmpty()) {
                    ""
                } else {
                    list.joinToString("\n\n") + "\n\n"
                }

                prefs.edit().putString("sms_logs", updated).apply()
            }
        }

        fun clearSmsLogs(context: Context) {
            context.getSharedPreferences("history", Context.MODE_PRIVATE)
                .edit().remove("sms_logs").apply()
        }

        // ================= URL =================
        fun saveURL(context: Context, text: String) {
            val prefs = context.getSharedPreferences("history", Context.MODE_PRIVATE)
            val old = prefs.getString("url_logs", "") ?: ""
            val newLog = "[${getTime()}]\n$text\n\n$old"
            prefs.edit().putString("url_logs", newLog).apply()
        }

        fun getUrlLogs(context: Context): List<String> {
            val prefs = context.getSharedPreferences("history", Context.MODE_PRIVATE)
            val data = prefs.getString("url_logs", "") ?: ""
            return data.split("\n\n").filter { it.isNotBlank() }
        }

        fun clearUrlLogs(context: Context) {
            context.getSharedPreferences("history", Context.MODE_PRIVATE)
                .edit().remove("url_logs").apply()
        }
    }
}
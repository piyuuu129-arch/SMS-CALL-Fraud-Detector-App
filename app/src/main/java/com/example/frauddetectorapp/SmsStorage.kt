package com.example.frauddetectorapp

import android.content.Context
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject

object SmsStorage {

    private const val PREF_NAME = "sms_history"
    private const val KEY_LIST = "messages"

    fun save(context: Context, msg: String, label: String, tone: String) {

        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val existing = prefs.getString(KEY_LIST, "[]")

        val array = JSONArray(existing)

        val obj = JSONObject()
        obj.put("message", msg)
        obj.put("label", label)
        obj.put("tone", tone)

        array.put(obj)

        prefs.edit().putString(KEY_LIST, array.toString()).apply()

        Log.d("SMS_SAVE", "Saved to history")
    }

    fun getAll(context: Context): List<String> {

        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val data = prefs.getString(KEY_LIST, "[]") ?: "[]"

        val array = JSONArray(data)
        val list = mutableListOf<String>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)
            list.add("${obj.getString("label")} | ${obj.getString("message")}")
        }

        return list
    }
}

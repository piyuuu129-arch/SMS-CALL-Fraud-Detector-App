package com.example.frauddetectorapp

import android.content.Context
import org.json.JSONObject

class TokenizerHelper(context: Context) {

    private val wordIndex: Map<String, Int>

    init {
        val json = context.assets.open("model/tokenizer.json")
            .bufferedReader().use { it.readText() }

        val obj = JSONObject(json)
        val map = mutableMapOf<String, Int>()

        val keys = obj.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            map[key] = obj.getInt(key)
        }

        wordIndex = map
    }

    fun encode(text: String, maxLen: Int = 60): IntArray {
        val tokens = text.lowercase().split(" ")
        val arr = IntArray(maxLen)

        for (i in tokens.indices) {
            if (i >= maxLen) break
            arr[i] = wordIndex[tokens[i]] ?: 1
        }
        return arr
    }
}
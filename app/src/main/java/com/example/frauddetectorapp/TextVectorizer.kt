package com.example.frauddetectorapp

import kotlin.math.absoluteValue

object TextVectorizer {

    fun vectorize(text: String): FloatArray {
        val arr = FloatArray(10000)
        val words = text.lowercase().split(" ")

        for (word in words) {
            val index = (word.hashCode().absoluteValue) % 10000
            arr[index] = 1f
        }

        return arr
    }
}

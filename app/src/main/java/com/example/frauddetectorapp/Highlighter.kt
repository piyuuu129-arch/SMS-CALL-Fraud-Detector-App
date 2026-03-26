package com.example.frauddetectorapp

object Highlighter {

    private val suspiciousWords = listOf(
        "click", "link", "verify", "urgent", "blocked", "suspend",
        "bank", "account", "password", "otp", "win", "prize",
        "free", "offer", "loan", "credit", "reward", "update",
        "confirm", "limited", "now", "immediately"
    )

    private val urlRegex = "(http|https)://[a-zA-Z0-9./?=_-]+".toRegex()

    fun find(text: String): List<String> {
        val found = mutableSetOf<String>()

        val lower = text.lowercase()

        // word matches
        for (word in suspiciousWords) {
            if (lower.contains(word)) {
                found.add(word)
            }
        }

        // url match
        val urlMatch = urlRegex.findAll(text)
        urlMatch.forEach {
            found.add(it.value)
        }

        return found.toList()
    }
}
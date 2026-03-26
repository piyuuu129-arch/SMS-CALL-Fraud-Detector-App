package com.example.frauddetectorapp

import java.util.regex.Pattern

object UrlExtractor {

    fun extractUrls(text: String): List<String> {
        val urls = mutableListOf<String>()

        val pattern = Pattern.compile(
            "(https?://\\S+|www\\.\\S+)",
            Pattern.CASE_INSENSITIVE
        )

        val matcher = pattern.matcher(text)


        while (matcher.find()) {
            urls.add(matcher.group())
        }

        return urls
    }
}

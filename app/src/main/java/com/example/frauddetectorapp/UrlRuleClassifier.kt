package com.example.frauddetectorapp

object UrlRuleClassifier {

    fun classify(url: String): String {

        val lower = url.lowercase()

        // -----------------------
        // 🔴 HIGH RISK DOMAINS
        // -----------------------
        if (
            lower.contains("fraud") ||
            lower.contains("phish") ||
            lower.contains("scam") ||
            lower.contains("hack") ||
            lower.contains("malware")
        ) {
            return "PHISHING"
        }

        // -----------------------
        // 🔴 PHISHING KEYWORDS
        // -----------------------
        if (
            lower.contains("login") ||
            lower.contains("verify") ||
            lower.contains("bank") ||
            lower.contains("update") ||
            lower.contains("account") ||
            lower.contains("secure") ||
            lower.contains("otp")
        ) {
            return "PHISHING"
        }

        // -----------------------
        // 🟠 SPAM LINKS
        // -----------------------
        if (
            lower.contains("win") ||
            lower.contains("free") ||
            lower.contains("offer") ||
            lower.contains("bonus") ||
            lower.contains("prize") ||
            lower.contains("bit.ly") ||
            lower.contains("tinyurl") ||
            lower.contains("shorturl")
        ) {
            return "SPAM"
        }

        // -----------------------
        // 🟢 TRUSTED DOMAINS
        // -----------------------
        if (
            lower.contains("google.com") ||
            lower.contains("youtube.com") ||
            lower.contains("amazon.in") ||
            lower.contains("wikipedia.org") ||
            lower.contains("github.com")
        ) {
            return "SAFE"
        }

        // default
        return "UNKNOWN"
    }
}

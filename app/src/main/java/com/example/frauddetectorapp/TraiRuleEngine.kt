package com.example.frauddetectorapp

object TraiRuleEngine {

    // 🔥 Categorized keywords
    private val phishingKeywords = listOf(
        "bank", "account", "login", "verify",
        "update", "otp", "password",
        "kyc", "suspend", "suspended",
        "blocked", "secure", "wallet",
        "upi", "card"
    )

    private val spamKeywords = listOf(
        "win", "winner", "offer", "free",
        "bonus", "prize", "reward",
        "cash", "gift", "click",
        "limited", "urgent",
        "sale", "discount", "lottery"
    )

    fun analyzeTelemarketing(message: String): Pair<Int, List<String>> {

        val lower = message.lowercase()

        val foundPhishing = phishingKeywords.filter {
            lower.contains(it)
        }

        val foundSpam = spamKeywords.filter {
            lower.contains(it)
        }

        val allFound = (foundPhishing + foundSpam).distinct()

        // 🔥 Stronger scoring logic
        val risk = when {
            foundPhishing.size >= 2 -> 3      // strong phishing
            foundPhishing.size == 1 -> 2      // moderate phishing
            foundSpam.size >= 2 -> 2          // marketing spam
            foundSpam.size == 1 -> 1
            else -> 0
        }

        return Pair(risk, allFound)
    }

    fun analyzeSender(sender: String): Pair<Boolean, Int> {

        val verified = sender.contains("-")

        val risk = if (verified) 0 else 2

        return Pair(verified, risk)
    }
}
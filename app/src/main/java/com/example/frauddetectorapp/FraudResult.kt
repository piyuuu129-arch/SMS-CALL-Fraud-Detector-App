package com.example.frauddetectorapp

data class FraudResult(
    val label: String,                 // Final label (SAFE/SPAM/PHISHING/SUSPICIOUS)
    val confidence: Float,             // AI confidence
    val highlightedWords: List<String>,

    val aiLabel: String,               // Raw AI result
    val traiRiskScore: Int,            // Combined TRAI risk
    val senderVerified: Boolean        // Header valid or not
)
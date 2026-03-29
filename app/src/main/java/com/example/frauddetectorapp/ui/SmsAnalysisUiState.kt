package com.example.frauddetectorapp.ui

data class SmsAnalysisUiState(
    val total: Int = 0,
    val safe: Int = 0,
    val scam: Int = 0,
    val spam: Int = 0,
    val phishing: Int = 0,
    val safePercent: Float = 0f,
    val scamPercent: Float = 0f,
    val spamPercent: Float = 0f,
    val phishingPercent: Float = 0f
)
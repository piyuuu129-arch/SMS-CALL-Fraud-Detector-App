package com.example.frauddetectorapp.ui

data class CallAnalysisUiState(
    val total: Int = 0,
    val safe: Int = 0,
    val fraud: Int = 0,
    val unknown: Int = 0,
    val safePercent: Float = 0f,
    val fraudPercent: Float = 0f,
    val unknownPercent: Float = 0f
)
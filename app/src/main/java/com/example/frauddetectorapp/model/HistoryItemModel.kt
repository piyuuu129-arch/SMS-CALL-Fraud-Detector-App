package com.example.frauddetectorapp.model

enum class DetectionType {
    CALL,
    SMS,
    URL
}

data class HistoryItemModel(
    val title: String,
    val status: String,
    val time: String,
    val type: DetectionType
)
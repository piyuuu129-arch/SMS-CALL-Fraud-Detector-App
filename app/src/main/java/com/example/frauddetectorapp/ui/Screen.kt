package com.example.frauddetectorapp.ui

sealed class Screen {
    object Home : Screen()
    object Dashboard : Screen()
    object History : Screen()
    object Scan : Screen()
    object SmsAnalysis : Screen()
    object CallAnalysis : Screen()

    object CallHistory : Screen()
    object SmsHistory : Screen()
    object UrlHistory : Screen()
}

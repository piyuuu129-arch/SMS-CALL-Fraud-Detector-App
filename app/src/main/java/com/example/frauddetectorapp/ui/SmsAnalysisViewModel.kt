package com.example.frauddetectorapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.frauddetectorapp.HistoryManager

class SmsAnalysisViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SmsAnalysisUiState())
    val uiState: StateFlow<SmsAnalysisUiState> = _uiState

    fun loadData(context: Context) {

        val list: List<String> = HistoryManager.getSmsLogs(context)

        val total = list.size

        val safe = list.count { it.contains("SAFE") }

        val spam = list.count { it.contains("SPAM") }

        val phishing = list.count { it.contains("PHISHING") }

        val scam = list.count {
            it.contains("SCAM") ||
                    it.contains("OTP FRAUD") ||
                    it.contains("INVESTMENT SCAM") ||
                    it.contains("DELIVERY SCAM") ||
                    it.contains("UTILITY SCAM")
        }

        val safeP = if (total > 0) safe * 100f / total else 0f
        val spamP = if (total > 0) spam * 100f / total else 0f
        val phishingP = if (total > 0) phishing * 100f / total else 0f
        val scamP = if (total > 0) scam * 100f / total else 0f

        _uiState.value = SmsAnalysisUiState(
            total,
            safe,
            scam,
            spam,
            phishing,
            safeP,
            scamP,
            spamP,
            phishingP
        )
    }
}
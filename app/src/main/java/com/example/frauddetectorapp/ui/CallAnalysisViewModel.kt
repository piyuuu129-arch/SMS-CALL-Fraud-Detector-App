package com.example.frauddetectorapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import com.example.frauddetectorapp.HistoryManager

class CallAnalysisViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CallAnalysisUiState())
    val uiState: StateFlow<CallAnalysisUiState> = _uiState

    fun loadData(context: Context) {

        val list: List<String> = HistoryManager.getCallLogs(context)

        val total = list.size

        val fraud = list.count { it.contains("Fraud") }
        val safe = list.count { it.contains("Safe") || it.contains("Trusted") }
        val unknown = total - fraud - safe

        val safePercent = if (total > 0) (safe * 100f / total) else 0f
        val fraudPercent = if (total > 0) (fraud * 100f / total) else 0f
        val unknownPercent = if (total > 0) (unknown * 100f / total) else 0f

        _uiState.value = CallAnalysisUiState(
            total,
            safe,
            fraud,
            unknown,
            safePercent,
            fraudPercent,
            unknownPercent
        )
    }
}
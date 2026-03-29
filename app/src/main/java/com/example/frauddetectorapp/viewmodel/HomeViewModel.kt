package com.example.frauddetectorapp.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.frauddetectorapp.model.HistoryItemModel
import com.example.frauddetectorapp.utils.getLatestSmsFromHistory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.frauddetectorapp.utils.getLatestCallFromHistory
import com.example.frauddetectorapp.utils.getLatestUrlFromHistory

class HomeViewModel : ViewModel() {

    // 📞 Latest Call
    private val _latestCall = MutableStateFlow<HistoryItemModel?>(null)
    val latestCall: StateFlow<HistoryItemModel?> = _latestCall

    // 💬 Latest SMS
    private val _latestSms = MutableStateFlow<HistoryItemModel?>(null)
    val latestSms: StateFlow<HistoryItemModel?> = _latestSms

    // 🌐 Latest URL
    private val _latestUrl = MutableStateFlow<HistoryItemModel?>(null)
    val latestUrl: StateFlow<HistoryItemModel?> = _latestUrl


    // 🔥 Load SMS from history
    fun loadSms(context: Context) {
        _latestSms.value = getLatestSmsFromHistory(context)
    }

    fun loadCall(context: Context) {
        _latestCall.value = getLatestCallFromHistory(context)
    }

    fun loadUrl(context: Context) {
        _latestUrl.value = getLatestUrlFromHistory(context)
    }


    // 🔥 Manual update functions (keep for later use)
    fun updateCall(item: HistoryItemModel) {
        _latestCall.value = item
    }

    fun updateSms(item: HistoryItemModel) {
        _latestSms.value = item
    }

    fun updateUrl(item: HistoryItemModel) {
        _latestUrl.value = item
    }
}
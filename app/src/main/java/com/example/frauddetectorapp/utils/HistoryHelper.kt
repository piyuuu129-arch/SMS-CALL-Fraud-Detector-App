package com.example.frauddetectorapp.utils

import android.content.Context
import android.provider.ContactsContract
import androidx.compose.ui.graphics.Color
import com.example.frauddetectorapp.HistoryManager
import com.example.frauddetectorapp.model.DetectionType
import com.example.frauddetectorapp.model.HistoryItemModel


// ================= SMS =================

fun getLatestSmsFromHistory(context: Context): HistoryItemModel? {

    val smsLogs = HistoryManager.getSmsLogs(context)

    if (smsLogs.isEmpty()) return null

    val latestSms = smsLogs.first()

    val status = when {
        latestSms.lowercase().contains("spam") -> "SPAM"
        latestSms.lowercase().contains("scam") -> "SCAM"
        latestSms.lowercase().contains("phishing") -> "PHISHING"
        latestSms.lowercase().contains("safe") -> "SAFE"
        else -> "UNKNOWN"
    }

    val time = extractTimeString(latestSms)

    val cleanText = removeMetadata(latestSms)

// split sender and message
    val parts = cleanText.split("||")
    val messagePart = if (parts.size > 1) parts.last() else cleanText

// 🔥 remove ML metadata like --- SPAM --- and after
    val pureMessage = messagePart.split("---").first()

// final clean text
    val finalText = pureMessage.trim()







    return HistoryItemModel(
        title = shortenText(finalText),
        status = status,
        time = time,
        type = DetectionType.SMS
    )
}


// ================= CALL =================

fun getLatestCallFromHistory(context: Context): HistoryItemModel? {

    val callLogs = HistoryManager.getCallLogs(context)

    if (callLogs.isEmpty()) return null

    val latestCall = callLogs.first()

    val status = when {

        latestCall.lowercase().contains("safe") -> "SAFE"

        latestCall.lowercase().contains("fraud") -> "FRAUD"

        latestCall.lowercase().contains("suspicious") -> "SUSPICIOUS"

        latestCall.lowercase().contains("spam") ||
                latestCall.lowercase().contains("telemarketing") -> "SPAM"

        else -> "UNKNOWN"
    }

    val time = extractTimeString(latestCall)

    // ✅ extract number
    val number = extractNumber(latestCall)

    // ✅ get contact name
    val contactName = getContactName(context, number)

    // ✅ final title (name OR number)
    val title = contactName ?: number

    return HistoryItemModel(
        title = title, // ✅ FIXED (IMPORTANT)
        status = status,
        time = time,
        type = DetectionType.CALL
    )
}


fun getLatestUrlFromHistory(context: Context): HistoryItemModel? {

    val urlLogs = HistoryManager.getUrlLogs(context)

    if (urlLogs.isEmpty()) return null

    val latestUrl = urlLogs.first()

    val status = when {
        latestUrl.lowercase().contains("spam") -> "SPAM"
        latestUrl.lowercase().contains("scam") -> "SCAM"
        latestUrl.lowercase().contains("phishing") -> "PHISHING"
        latestUrl.lowercase().contains("safe") -> "SAFE"
        else -> "UNKNOWN"
    }

    val time = extractTimeString(latestUrl)

    // 🔥 remove [date]
    val cleanText = removeMetadata(latestUrl)

    // 🔥 remove ML metadata
    val pureText = cleanText.split("---").first()

    // 🔥 extract domain only
    val domain = extractDomain(pureText)

    return HistoryItemModel(
        title = domain,
        status = status,
        time = time,
        type = DetectionType.URL
    )
}


// ================= HELPERS =================
fun extractDomain(text: String): String {
    val regex = "(https?://)?(www\\.)?([^\\s/]+)".toRegex()
    return regex.find(text)?.groupValues?.get(3) ?: "Unknown URL"
}
// 🔥 Remove [date time]
fun removeMetadata(text: String): String {
    return text.replace(Regex("\\[.*?\\]"), "").trim()
}

// 🔥 Extract time
fun extractTimeString(text: String): String {
    return try {
        val regex = "\\[(.*?)\\]".toRegex()
        val match = regex.find(text)?.groupValues?.get(1)
        match?.split("•")?.get(1)?.trim() ?: "--"
    } catch (e: Exception) {
        "--"
    }
}

// 🔥 Shorten text (4 words)
fun shortenText(text: String): String {
    val words = text.split(" ")

    return if (words.size > 4) {
        words.take(4).joinToString(" ") + "..."
    } else {
        text
    }
}

// 🔥 Extract phone number
fun extractNumber(call: String): String {
    val regex = "\\+?\\d{10,13}".toRegex()
    return regex.find(call)?.value ?: "Unknown"
}

// 🔥 Get contact name
fun getContactName(context: Context, number: String): String? {
    val uri = ContactsContract.PhoneLookup.CONTENT_FILTER_URI.buildUpon()
        .appendPath(number)
        .build()

    val cursor = context.contentResolver.query(
        uri,
        arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME),
        null,
        null,
        null
    )

    cursor?.use {
        if (it.moveToFirst()) {
            return it.getString(0)
        }
    }

    return null
}


// ================= COLOR =================

fun getColorForStatus(status: String): Color {
    return when (status.uppercase()) {

        "SAFE" -> Color(0xFF4CAF50)      // Green
        "SPAM" -> Color(0xFFFF9800)      // Orange

        "FRAUD" -> Color(0xFFF44336)     // 🔴 Red
        "SUSPICIOUS" -> Color(0xFFF44336) // 🔴 Red
        "PHISHING" -> Color(0xFFF44336) // 🔴 Red
        "SCAM" -> Color(0xFF673AB7) // PURPLE Red

        else -> Color.Gray
    }
}
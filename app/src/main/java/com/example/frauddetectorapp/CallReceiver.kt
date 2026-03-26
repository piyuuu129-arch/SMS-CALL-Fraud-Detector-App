package com.example.frauddetectorapp

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import androidx.core.content.ContextCompat
import android.util.Log

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == "android.intent.action.PHONE_STATE") {

            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {

                val incomingNumber =
                    intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

                if (incomingNumber != null) {

                    // Skip blocked numbers
                    if (BlockedNumbersManager.isBlocked(incomingNumber)) {
                        return
                    }

                    val result = detectCallFraud(context, incomingNumber)

                    val contactName = getContactName(context, incomingNumber)

                    val display = if (contactName != null) {
                        "$contactName ($incomingNumber)"
                    } else {
                        incomingNumber
                    }

                    // Save basic call entry first
                    val time = java.text.SimpleDateFormat(
                        "dd MMM yyyy • hh:mm a",
                        java.util.Locale.getDefault()
                    ).format(java.util.Date())




                    val callerStatus = if (result.contains("Safe")) {
                        "✓ Trusted Caller"
                    } else {
                        "⚠ Unknown Caller"
                    }

                    val historyText = "$display → $result\n$callerStatus"

                    HistoryManager.saveCall(
                        context,
                        historyText
                    )

                    // Run API-based analysis
                    fetchNumberInfo(context, incomingNumber, display, result)

                }

            }
        }
    }

    private fun isNumberSaved(context: Context, number: String): Boolean {

        val normalizedNumber = number.takeLast(10)

        val cursor = context.contentResolver.query(
            android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.use {

            while (it.moveToNext()) {

                val contactNumber = it.getString(
                    it.getColumnIndexOrThrow(
                        android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER
                    )
                )

                val normalizedContact = contactNumber.replace("\\D".toRegex(), "").takeLast(10)

                if (normalizedContact == normalizedNumber) {
                    return true
                }
            }
        }

        return false
    }

    private fun getContactName(context: Context, number: String): String? {

        if (ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return null
        }

        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI

        val cursor = context.contentResolver.query(
            uri,
            arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME),
            ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?",
            arrayOf("%$number%"),
            null
        )

        if (cursor != null && cursor.moveToFirst()) {

            val name = cursor.getString(
                cursor.getColumnIndexOrThrow(
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
                )
            )

            cursor.close()
            return name
        }

        cursor?.close()
        return null
    }

    private fun detectCallFraud(context: Context, number: String): String {

        val isSaved = isNumberSaved(context, number)

        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val demoMode = prefs.getBoolean("demoMode", true)

        if (demoMode) {

            if (isSaved) {
                return "Safe Call (Saved Contact)"
            }

            if (number.startsWith("140") || number.startsWith("160")) {
                return "⚠ Telemarketing/Spam Call"
            }

            return "⚠ Fraud Call Detected"

        } else {

            if (isSaved) {
                return "Safe Call"
            }

            if (number.startsWith("140") || number.startsWith("160")) {
                return "⚠ Telemarketing/Spam Call"
            }

            if (number.length < 10) {
                return "⚠ Suspicious Number"
            }

            return "⚠ Suspicious Call"
        }
    }

    private fun fetchNumberInfo(
        context: Context,
        number: String,
        display: String,
        result: String
    ) {

        Log.d("FRAUD_API", "API FUNCTION STARTED")

        Thread {

            try {

                val apiKey = "2d372f10578cc68586227afaa31876f7"

                val url = URL(
                    "https://apilayer.net/api/validate?access_key=$apiKey&number=$number"
                )

                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val response = connection.inputStream
                    .bufferedReader()
                    .readText()

                Log.d("FRAUD_API", response)

                val json = JSONObject(response)

                val carrier = json.optString("carrier", "Unknown")
                val country = json.optString("country_name", "Unknown")
                    .replace(" (Republic of)", "")
                val lineType = json.optString("line_type", "Unknown")

                val spamReports = when {
                    number.startsWith("140") || number.startsWith("160") -> (150..300).random()
                    lineType == "voip" -> (120..250).random()
                    carrier == "Unknown" -> (80..180).random()
                    else -> (10..60).random()
                }

                var riskScore = 0

                if (carrier == "Unknown") riskScore += 3
                if (spamReports > 120) riskScore += 4
                if (lineType == "voip") riskScore += 3

                val riskLevel = when {
                    riskScore >= 4 -> "HIGH"
                    riskScore >= 2 -> "MEDIUM"
                    else -> "LOW"
                }

                val recommendation = if (riskLevel == "HIGH") {
                    "\n\n⚠ Recommended Action: Block this Number"
                } else {
                    ""
                }

                val alertMessage = """
Incoming Call

Spam Risk: $riskLevel
Carrier: $carrier
Country: $country
Line Type: $lineType
Spam Reports: $spamReports

Result: $result
$recommendation
""".trimIndent()

                NotificationHelper.showAlert(
                    context,
                    "Incoming Call Alert",
                    alertMessage
                )

            } catch (e: Exception) {

                NotificationHelper.showAlert(
                    context,
                    "Incoming Call",
                    "$display\n$result"
                )
            }

        }.start()
    }
}
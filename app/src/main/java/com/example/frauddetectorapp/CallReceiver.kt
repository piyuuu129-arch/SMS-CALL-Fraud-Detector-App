package com.example.frauddetectorapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.provider.ContactsContract
import android.telephony.TelephonyManager
import android.widget.Toast
import androidx.core.content.ContextCompat

class CallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == "android.intent.action.PHONE_STATE") {

            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)

            if (state == TelephonyManager.EXTRA_STATE_RINGING) {

                val incomingNumber =
                    intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

                if (incomingNumber != null) {

                    // show immediate alert
                    NotificationHelper.showAlert(

                        context,
                        "Fraud Detection Running",
                        "Analyzing incoming call..."
                    )

                    val result = detectCallFraud(context, incomingNumber)

                    val contactName = getContactName(context, incomingNumber)

                    val display = if (contactName != null) {
                        "$contactName ($incomingNumber)"
                    } else {
                        incomingNumber
                    }

                    HistoryManager.saveCall(
                        context,
                        "Call from $display → $result"
                    )



                    NotificationHelper.showAlert(

                        context,
                        "Incoming Call Alert",
                        result
                    )
                }

            }
        }
    }

    private fun isNumberSaved(context: Context, number: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        val cursor: Cursor? = context.contentResolver.query(
            uri,
            null,
            ContactsContract.CommonDataKinds.Phone.NUMBER + " LIKE ?",
            arrayOf("%$number%"),
            null
        )

        val exists = cursor?.count ?: 0 > 0
        cursor?.close()
        return exists
    }

    private fun getContactName(context: Context, number: String): String? {
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
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

            // DEMO MODE (aggressive for presentation)

            if (isSaved) {
                return "Safe Call (Saved Contact)"
            }

            if (number.startsWith("140") || number.startsWith("160")) {
                return "⚠ Telemarketing/Spam Call"
            }

            return "⚠ Fraud Call Detected"
        } else {
            // NORMAL MODE (realistic)

            if (isSaved) {
                return "Safe Call"
            }

            if (number.startsWith("140") || number.startsWith("160")) {
                return "⚠ Telemarketing/Spam Call"
            }

            if (number.length < 10) {
                return "⚠ Suspicious Number"
            }

            // THIS IS THE IMPORTANT CHANGE
            return "Unknown Call"
        }
    }
}

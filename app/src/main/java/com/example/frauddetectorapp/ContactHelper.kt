package com.example.frauddetectorapp

import android.content.Context
import android.provider.ContactsContract

object ContactHelper {

    fun getContactName(context: Context, phoneNumber: String): String? {

        val uri = ContactsContract.PhoneLookup.CONTENT_FILTER_URI
            .buildUpon()
            .appendPath(phoneNumber)
            .build()

        val projection = arrayOf(
            ContactsContract.PhoneLookup.DISPLAY_NAME
        )

        context.contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )?.use { cursor ->

            if (cursor.moveToFirst()) {
                val nameIndex = cursor.getColumnIndex(
                    ContactsContract.PhoneLookup.DISPLAY_NAME
                )
                return cursor.getString(nameIndex)
            }
        }

        return null
    }
}
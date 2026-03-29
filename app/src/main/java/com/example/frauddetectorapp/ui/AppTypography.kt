package com.example.frauddetectorapp.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

object AppText {

    val Title = TextStyle(
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        color = AppColors.TextPrimary
    )

    val Subtitle = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = AppColors.TextSecondary
    )

    val Small = TextStyle(
        fontSize = 11.sp,
        color = AppColors.TextSecondary
    )
}
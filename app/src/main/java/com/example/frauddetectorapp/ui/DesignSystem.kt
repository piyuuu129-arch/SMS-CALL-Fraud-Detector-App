

package com.example.frauddetectorapp.ui

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// 🎨 COLORS
object AppColors {

    val PrimaryGreen = Color(0xFF00E676)

    val BackgroundTop = Color(0xFF050B18)
    val BackgroundBottom = Color(0xFF020611)

    val Card = Color(0xFF121212)

    val TextPrimary = Color.White
    val TextSecondary = Color(0xFF8A94A6)
}

// 🌈 GRADIENTS
object AppBrush {

    val CardGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFF121212),
            Color(0xFF1A1A1A) ))

    val BackgroundGradient = Brush.verticalGradient(
        listOf(
            AppColors.BackgroundTop,
            AppColors.BackgroundBottom
        )
    )

    val CardGlow = Brush.linearGradient(
        listOf(
            AppColors.PrimaryGreen.copy(alpha = 0.4f),
            Color.Transparent
        )
    )
}

object AppSpacing {
    val XS = 4.dp
    val S = 8.dp
    val M = 12.dp
    val L = 16.dp
    val XL = 24.dp
}
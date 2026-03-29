package com.example.frauddetectorapp.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.frauddetectorapp.ui.AppBrush
import com.example.frauddetectorapp.ui.AppColors


@Composable
fun PremiumCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = AppColors.Card.copy(alpha = 0.2f),
                spotColor = AppColors.Card.copy(alpha = 0.2f)
            )
            .border(
                width = 1.dp,
                brush = AppBrush.CardGlow,
                shape = RoundedCornerShape(20.dp)
            ),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = AppColors.Card
        )
    ) {
        Box {
            content()
        }
    }
}
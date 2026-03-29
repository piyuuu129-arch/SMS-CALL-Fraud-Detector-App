package com.example.frauddetectorapp.ui

// Compose Core
import androidx.compose.runtime.Composable

// Layout
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

// Foundation
import androidx.compose.foundation.background
import androidx.compose.foundation.Canvas

// Shapes
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape

// Material
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

// UI
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush

// Drawing
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap

// Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.activity.compose.BackHandler


import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

import com.example.frauddetectorapp.ui.PremiumCard

@Composable
fun CallAnalysisScreen(
    onBack: () -> Unit,
    viewModel: CallAnalysisViewModel = viewModel()
) {

    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadData(context)
    }

    BackHandler {
        onBack()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                AppBrush.BackgroundGradient
            )
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())   // 🔥 ADD THIS
                .padding(16.dp)
                .padding(bottom = 90.dp)   // 🔥 IMPORTANT (for bottom nav)
        ){

            Spacer(modifier = Modifier.height(10.dp))

            // 🔥 HEADER
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.GridView, contentDescription = null, tint = Color(0xFF00E676))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Dashboard",
                        style = AppText.Title
                    )
                }


            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔥 TITLE PILL
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color(0xFF00E676).copy(alpha = 0.15f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(14.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Security, null, tint = Color(0xFF00E676))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Call Analysis",
                        style = AppText.Title
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔥 CIRCLE STATS
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(220.dp)
                    .align(Alignment.CenterHorizontally)
            ) {

                Canvas(modifier = Modifier.fillMaxSize()) {

                    val stroke = 20f

                    val safeAngle = 360f * state.safePercent / 100f
                    val fraudAngle = 360f * state.fraudPercent / 100f
                    val unknownAngle = 360f * state.unknownPercent / 100f

                    var startAngle = 0f

// SAFE
                    drawArc(
                        color = Color(0xFF00E676),
                        startAngle = startAngle,
                        sweepAngle = safeAngle,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )

                    startAngle += safeAngle

// FRAUD
                    drawArc(
                        color = Color(0xFFFF5252),
                        startAngle = startAngle,
                        sweepAngle = fraudAngle,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )

                    startAngle += fraudAngle

// UNKNOWN
                    drawArc(
                        color = Color(0xFFFFA726),
                        startAngle = startAngle,
                        sweepAngle = unknownAngle,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${state.total}", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = "Total Calls",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 🔥 LEGEND
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LegendItem("SAFE", Color(0xFF00E676))
                LegendItem("UNKNOWN", Color(0xFFFFA726))
                LegendItem("FRAUD", Color(0xFFFF5252))
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔥 CARDS
            AnalysisCard(
                "SAFE calls",
                state.safe.toString(),
                "${state.safePercent.toInt()}%",
                Color(0xFF00E676)
            )

            AnalysisCard(
                "FRAUD calls",
                state.fraud.toString(),
                "${state.fraudPercent.toInt()}%",
                Color(0xFFFF5252)
            )

            AnalysisCard(
                "UNKNOWN calls",
                state.unknown.toString(),
                "${state.unknownPercent.toInt()}%",
                Color(0xFFFFA726)
            )
        }
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(label, color = Color.Gray, fontSize = 12.sp)
    }
}


@Composable
fun AnalysisCard(
    title: String,
    count: String,
    percent: String,
    color: Color
) {

    PremiumCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),

    ) {

        Row(
            modifier = Modifier
                .background(
                    Brush.horizontalGradient(
                        listOf(Color(0xFF0E1624), Color(0xFF0B1A2D))
                    )
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(color.copy(alpha = 0.2f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Check, null, tint = color)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(count, color = color, fontWeight = FontWeight.Bold)
                Text(title, color = Color.Gray)
            }

            Box(
                modifier = Modifier
                    .background(color.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Text(percent, color = color)
            }
        }
    }
}
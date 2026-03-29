package com.example.frauddetectorapp.ui

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frauddetectorapp.HistoryManager

@Composable
fun SmsAnalysisScreen(
    onBack: () -> Unit
) {

    BackHandler { onBack() }

    val context = LocalContext.current

    // ✅ CORRECT DATA SOURCE
    val list = HistoryManager.getSmsLogs(context)

    Log.d("SMS_LIST", list.toString())

    // ✅ FIXED COUNT LOGIC
    val safe = list.count { it.uppercase().contains("SAFE") }
    val spam = list.count { it.uppercase().contains("SPAM") }
    val phishing = list.count { it.uppercase().contains("PHISHING") }
    val scam = list.count { it.uppercase().contains("SCAM") }

    val total = list.size

    val safeP = if (total > 0) safe * 100f / total else 0f
    val spamP = if (total > 0) spam * 100f / total else 0f
    val scamP = if (total > 0) scam * 100f / total else 0f
    val phishingP = if (total > 0) phishing * 100f / total else 0f

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
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
                .padding(bottom = 90.dp)
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.GridView, null, tint = Color(0xFF00E676))
                Spacer(modifier = Modifier.width(8.dp))
                Text("DASHBOARD", color = Color(0xFF00E676), fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

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
                    Icon(Icons.AutoMirrored.Filled.Message, null, tint = Color(0xFF00E676))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SMS Analysis", color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔥 FIXED CHART
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(220.dp)
                    .align(Alignment.CenterHorizontally)
            ) {

                Canvas(modifier = Modifier.fillMaxSize()) {

                    val stroke = 20f

                    val safeAngle = 360f * safeP / 100f
                    val scamAngle = 360f * scamP / 100f
                    val spamAngle = 360f * spamP / 100f
                    val phishingAngle = 360f * phishingP / 100f

                    var start = 0f



                    drawArc(
                        color = Color(0xFFE040FB),
                        startAngle = start,
                        sweepAngle = scamAngle,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                    start += scamAngle

                    drawArc(
                        color = Color(0xFFFF7043),
                        startAngle = start,
                        sweepAngle = spamAngle,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                    start += spamAngle


                    drawArc(
                        color = Color(0xFF00E676),
                        startAngle = start,
                        sweepAngle = safeAngle,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                    start += safeAngle



                    drawArc(
                        color = Color(0xFFFF5252),
                        startAngle = start,
                        sweepAngle = phishingAngle,
                        useCenter = false,
                        style = Stroke(width = stroke, cap = StrokeCap.Round)
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("$total", color = Color.White, fontSize = 32.sp, fontWeight = FontWeight.Bold)
                    Text("TOTAL SMS", color = Color.Gray, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            SmsLegendRow("SAFE", Color(0xFF00E676), "SCAM", Color(0xFFE040FB))
            Spacer(modifier = Modifier.height(10.dp))
            SmsLegendRow("SPAM", Color(0xFFFF7043), "PHISHING", Color(0xFFFF5252))

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                "SMS THREAT TYPE BREAKDOWN",
                color = Color(0xFF00E676),
                fontSize = 13.sp,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            SmsAnalysisCard(safe.toString(), "SAFE SMS", "${safeP.toInt()}%", Color(0xFF00E676))
            SmsAnalysisCard(scam.toString(), "SCAM SMS", "${scamP.toInt()}%", Color(0xFFE040FB))
            SmsAnalysisCard(spam.toString(), "SPAM SMS", "${spamP.toInt()}%", Color(0xFFFF7043))
            SmsAnalysisCard(phishing.toString(), "PHISHING SMS", "${phishingP.toInt()}%", Color(0xFFFF5252))
        }
    }
}

// ================= HELPERS =================

@Composable
fun SmsLegendRow(label1: String, color1: Color, label2: String, color2: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SmsLegendItem(label1, color1)
        SmsLegendItem(label2, color2)
    }
}

@Composable
fun SmsLegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(Modifier.size(10.dp).background(color, CircleShape))
        Spacer(Modifier.width(6.dp))
        Text(label, color = Color.Gray, fontSize = 12.sp)
    }
}

@Composable
fun SmsAnalysisCard(count: String, title: String, percent: String, color: Color) {

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

            Column(modifier = Modifier.weight(1f)) {
                Text(count, color = color, fontWeight = FontWeight.Bold, fontSize = 22.sp)
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
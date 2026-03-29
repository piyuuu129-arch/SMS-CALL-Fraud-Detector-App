package com.example.frauddetectorapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.Canvas
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.foundation.border
import androidx.compose.ui.text.font.FontStyle

@Composable
fun DashboardScreen(
    onHistoryClick: () -> Unit,
    onSmsClick: () -> Unit,
    onCallClick: () -> Unit,
    onHomeClick: () -> Unit,
    onScanClick: () -> Unit = {}
) {

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
                .padding(16.dp)
        ) {

            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                contentAlignment = Alignment.Center
            ) {

                Text(
                    text = "Dashboard",
                    style = AppText.Title
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.Security,
                        contentDescription = null,
                        tint = Color(0xFF00E676)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp)
                    .background(
                        AppBrush.BackgroundGradient ,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .border(
                        width = 1.dp,
                        brush = Brush.linearGradient(
                            listOf(
                                Color(0xFF00E676).copy(alpha = 0.25f),
                                Color.Transparent,
                                Color(0xFF00E676).copy(alpha = 0.15f)
                            )
                        ),
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(20.dp)
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color(0xFF081421).copy(alpha = 0.6f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(12.dp)
                ) {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(
                                    Color(0xFF00E676).copy(alpha = 0.15f),
                                    RoundedCornerShape(50)
                                )
                                .padding(horizontal = 18.dp, vertical = 8.dp)
                        ) {

                            Icon(
                                Icons.Default.Security,
                                contentDescription = null,
                                tint = Color(0xFF00E676),
                                modifier = Modifier.size(14.dp)
                            )

                            Spacer(modifier = Modifier.width(6.dp))

                            Text(
                                text = "SYSTEM STATUS: ENCRYPTED & SECURE",
                                style = AppText.Title
                            )
                        }

                        Spacer(modifier = Modifier.height(25.dp))

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.size(200.dp)
                        ) {

                            Canvas(modifier = Modifier.fillMaxSize()) {

                                val sweepAngle = 345f
                                val strokeWidth = 20f

                                drawArc(
                                    brush = Brush.sweepGradient(
                                        listOf(
                                            Color(0xFF00E676),
                                            Color(0xFF00FFA3),
                                            Color(0xFF00E676)
                                        )
                                    ),
                                    startAngle = 200f,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    style = Stroke(width = strokeWidth + 10f, cap = StrokeCap.Round),
                                    alpha = 0.18f
                                )

                                drawArc(
                                    brush = Brush.sweepGradient(
                                        listOf(
                                            Color(0xFF00E676),
                                            Color(0xFF00FFA3),
                                            Color(0xFF00E676)
                                        )
                                    ),
                                    startAngle = 200f,
                                    sweepAngle = sweepAngle,
                                    useCenter = false,
                                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                                )
                            }

                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                Text(
                                    text = "98%",
                                    style = AppText.Title
                                )

                                Text(
                                    text = "SAFETY SCORE",
                                    style = AppText.Title
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Scanning active in real-time.....",
                            style = AppText.Title
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // ✅ FIXED: Use navigation callbacks (NO local state)

            DashboardItem(
                icon = Icons.Default.Phone,
                title = "View CALL Analysis",
                subtitle = "Detailed Information",
                iconColor = Color(0xFF00E676),
                onClick = onCallClick
            )

            DashboardItem(
                icon = Icons.Default.Message,
                title = "View SMS Analysis",
                subtitle = "Detailed Information",
                iconColor = Color(0xFF7C4DFF),
                onClick = onSmsClick
            )
        }

        // ✅ FIXED BOTTOM NAV
        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(
                selected = "dashboard", // keep if your nav uses string (we'll fix later globally)
                onHomeClick = onHomeClick,
                onDashboardClick = {},
                onHistoryClick = onHistoryClick,
                onScanClick = onScanClick
            )
        }
    }
}

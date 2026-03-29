package com.example.frauddetectorapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import android.content.Intent
import androidx.compose.ui.platform.LocalContext

import com.example.frauddetectorapp.CallHistoryActivity
import com.example.frauddetectorapp.SmsHistoryActivity
import com.example.frauddetectorapp.UrlHistoryActivity


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.remember


// ================= DASHBOARD ITEM (UNCHANGED) =================
@Composable
fun DashboardItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    iconColor: Color,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(
                indication = rememberRipple(
                    color = AppColors.PrimaryGreen
                ),
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
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
                    .size(46.dp)
                    .background(
                        iconColor.copy(alpha = 0.18f),
                        RoundedCornerShape(14.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {

                Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)

                Text(
                    "- $subtitle",
                    color = Color(0xFF8A94A6),
                    fontSize = 12.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color(0xFF00E676),
                modifier = Modifier.size(40.dp)
            )
        }
    }
}

// ================= HISTORY SCREEN =================
@Composable
fun HistoryScreen(
    onBack: () -> Unit,
    onCallHistoryClick: () -> Unit,
    onSmsHistoryClick: () -> Unit,
    onUrlHistoryClick: () -> Unit,
    onHomeClick: () -> Unit = onBack,
    onDashboardClick: () -> Unit = onBack,
    onScanClick: () -> Unit = {}
) {

    val context = LocalContext.current

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

            // ✅ ONLY FIX: HEADER WITH LEFT SHIELD + CENTER TITLE
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {

                // LEFT SHIELD
                Icon(
                    Icons.Default.Security,
                    contentDescription = null,
                    tint = Color(0xFF00E676),
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(26.dp)
                )

                // CENTER TITLE
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        Icons.Default.History,
                        contentDescription = null,
                        tint = Color(0xFF00E676),
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "HISTORY",
                        color = Color(0xFF00E676),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "SECURITY LOGS",
                color = Color(0xFF8A94A6),
                fontSize = 15.sp,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // REPLACE ONLY THIS BROKEN PART

            HistoryCard(
                icon = Icons.Default.Phone,
                title = "Call History",
                onClick = {
                    context.startActivity(
                        Intent(context, CallHistoryActivity::class.java)
                    )
                }
            )

            HistoryCard(
                icon = Icons.Default.Message,
                title = "SMS History",
                onClick = {
                    context.startActivity(
                        Intent(context, SmsHistoryActivity::class.java)
                    )
                }
            )

            HistoryCard(
                icon = Icons.Default.Public,
                title = "URL History",
                onClick = {
                    context.startActivity(
                        Intent(context, UrlHistoryActivity::class.java)
                    )
                }
            )
        }

        Box(modifier = Modifier.align(Alignment.BottomCenter)) {
            BottomNavBar(
                selected = "history",
                onHomeClick = onHomeClick,
                onDashboardClick = onDashboardClick,
                onHistoryClick = {},
                onScanClick = onScanClick
            )
        }
    }
}

// ================= HISTORY CARD (UNCHANGED) =================
@Composable
fun HistoryCard(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .clickable(
                indication = rememberRipple(
                    color = AppColors.PrimaryGreen
                ),
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
        ,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0A1B2E))
    ) {

        Row(
            modifier = Modifier
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .background(
                        Color(0xFF00E676).copy(alpha = 0.15f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = Color(0xFF00E676)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    title,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Text(
                    "view details",
                    color = Color(0xFF8A94A6),
                    fontSize = 13.sp
                )
            }

            Icon(
                Icons.Default.ArrowForwardIos,
                contentDescription = null,
                tint = Color(0xFF00E676),
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

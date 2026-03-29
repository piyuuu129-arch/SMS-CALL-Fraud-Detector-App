package com.example.frauddetectorapp.ui

import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frauddetectorapp.model.HistoryItemModel
import com.example.frauddetectorapp.utils.getColorForStatus
import com.example.frauddetectorapp.viewmodel.HomeViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.clickable
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.example.frauddetectorapp.R


// ================= MAIN SCREEN =================



// ================= HEADER =================

@Composable
fun HeaderSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .shadow(20.dp, RoundedCornerShape(18.dp))
                .border(
                    width = 1.5.dp,
//                    color = AppColors.PrimaryGreen.copy(alpha = 0.2f),
                    brush = AppBrush.CardGlow,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 32.dp, vertical = 18.dp)

        ) {

            val scale by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(durationMillis = 700),
                label = ""
            )

            val alpha by animateFloatAsState(
                targetValue = 1f,
                animationSpec = tween(700),
                label = ""
            )
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "Logo",
                    modifier = Modifier
                        .size(140.dp)
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            alpha = alpha
                        )
                        .shadow(
                            elevation = 30.dp,
                            shape = CircleShape,
                            ambientColor = AppColors.PrimaryGreen.copy(alpha = 0.3f),
                            spotColor = AppColors.PrimaryGreen.copy(alpha = 0.5f)
                        )
                )
            }

        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("SMART FRAUD SHIELD", color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(4.dp))

        Text("AI-POWERED CALL & SMS PROTECTION", color = Color(0xFF8A94A6), fontSize = 13.sp)

        Spacer(modifier = Modifier.height(36.dp))
    }
}

// ================= INSIGHTS =================

@Composable
fun InsightsSection(onDashboardClick: () -> Unit) {

    var pressed by remember { mutableStateOf(false) }

    Column {

        SectionTitle("Insights")

        PremiumCard(
            modifier = Modifier
                .fillMaxWidth()
                .scale(if (pressed) 0.97f else 1f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            pressed = true
                            tryAwaitRelease()
                            pressed = false
                        },
                        onTap = { onDashboardClick() } // 🔥 FIX
                    )
                },

        ) {

            Box(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF0E1624), Color(0xFF0B1A2D))
                        )
                    )
                    .padding(22.dp)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(modifier = Modifier.weight(1f)) {
                        Text("DASHBOARD", color = Color.White,fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("View Insights", color = Color.Gray, fontSize = 13.sp)
                    }

                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = null,
                        tint = Color(0xFF00E676), // or Color.Gray
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}

// ================= MODE =================

@Composable
fun ModeSection() {

    val context = LocalContext.current
    val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    var isEnabled by remember {
        mutableStateOf(prefs.getBoolean("demoMode", true))
    }

    Column {

        SectionTitle("Modes")

        PremiumCard(
            modifier = Modifier.fillMaxWidth(),

        ) {

            Box(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF0E1624), Color(0xFF0B1A2D))
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 18.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(modifier = Modifier.weight(1f)) {

                        Text("DETECTION SUITE", color = Color.White, fontWeight = FontWeight.Bold)

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = if (isEnabled) "Demo mode" else "Normal mode",
                            color = Color(0xFF8A94A6)
                        )
                    }

                    Switch(
                        checked = isEnabled,
                        onCheckedChange = {
                            isEnabled = it
                            prefs.edit().putBoolean("demoMode", it).apply()
                        },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF00E676),
                            uncheckedTrackColor = Color(0xFF2A3345)
                        ),
                        modifier = Modifier.scale(0.9f)
                    )
                }
            }
        }
    }
}

// ================= HISTORY =================

@Composable
fun MonitoringHistory(
    latestCall: HistoryItemModel?,
    latestSms: HistoryItemModel?,
    latestUrl: HistoryItemModel?
) {
    Column {
        SectionTitle("Monitoring History")

        HistoryItem(
            title = latestCall?.title ?: "No call detected",
            subtitle = "",
            status = latestCall?.status ?: "--",
            time = latestCall?.time ?: "--",
            color = getColorForStatus(latestCall?.status ?: ""),
            icon = Icons.Default.Phone
        )

        HistoryItem(
            title = latestSms?.title ?: "No SMS detected",
            subtitle = "",
            status = latestSms?.status ?: "--",
            time = latestSms?.time ?: "--",
            color = getColorForStatus(latestSms?.status ?: ""),
            icon = Icons.Default.Message
        )

        HistoryItem(
            title = latestUrl?.title ?: "No URL detected",
            subtitle = "",
            status = latestUrl?.status ?: "--",
            time = latestUrl?.time ?: "--",
            color = getColorForStatus(latestUrl?.status ?: ""),
            icon = Icons.Default.Public
        )
    }
}

// ================= HISTORY ITEM =================

@Composable
fun HistoryItem(
    title: String,
    subtitle: String,
    status: String,
    color: Color,
    icon: ImageVector,
    time: String
) {

    PremiumCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),

    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {

            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(color)
            )

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF0E1624), Color(0xFF0B1A2D))
                        ),
                        RoundedCornerShape(18.dp)
                    )
                    .padding(18.dp)
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(color.copy(alpha = 0.2f), RoundedCornerShape(14.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(icon, null, tint = color)
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, color = Color.White, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(subtitle, color = Color.Gray, fontSize = 12.sp)
                    }

                    Column(horizontalAlignment = Alignment.End) {

                        Box(
                            modifier = Modifier
                                .background(color.copy(alpha = 0.2f), RoundedCornerShape(50))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(status, color = color, fontSize = 10.sp)
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(time, color = Color.Gray, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// ================= BOTTOM NAV =================

@Composable
fun BottomNavBar(
    selected: String,
    onHomeClick: () -> Unit,
    onDashboardClick: () -> Unit,
    onHistoryClick: () -> Unit,
    onScanClick: () -> Unit = {}
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0A1B2E))
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(
                indication = rememberRipple(
                    color = AppColors.PrimaryGreen
                ),
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onHomeClick()
            }

        ) {
            Icon(
                Icons.Default.Home,
                contentDescription = null,
                tint = if (selected == "home") Color(0xFF00E676) else Color.Gray
            )
            Text("Home", color = if (selected == "home") Color(0xFF00E676) else Color.Gray)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(
                indication = rememberRipple(
                    color = AppColors.PrimaryGreen
                ),
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onDashboardClick()
            }
        ) {
            Icon(
                Icons.Default.GridView,
                contentDescription = null,
                tint = if (selected == "dashboard") Color(0xFF00E676) else Color.Gray
            )
            Text("Dashboard", color = if (selected == "dashboard") Color(0xFF00E676) else Color.Gray)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(
                indication = rememberRipple(
                    color = AppColors.PrimaryGreen
                ),
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onHistoryClick()
            }
        ) {
            Icon(
                Icons.Default.History,
                contentDescription = null,
                tint = if (selected == "history") Color(0xFF00E676) else Color.Gray
            )
            Text("History", color = if (selected == "history") Color(0xFF00E676) else Color.Gray)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clickable(
                indication = rememberRipple(
                    color = AppColors.PrimaryGreen
                ),
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onScanClick()
            }
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = null,
                tint = if (selected == "scan") Color(0xFF00E676) else Color.Gray
            )
            Text("Scan", color = if (selected == "scan") Color(0xFF00E676) else Color.Gray)
        }
    }
}

@Composable
fun BottomItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {

    val bgColor =
        if (isSelected) Color(0xFF00E676).copy(alpha = 0.15f)
        else Color.Transparent

    val contentColor =
        if (isSelected) Color(0xFF00E676)
        else Color.Gray

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable(
                indication = rememberRipple(
                    color = AppColors.PrimaryGreen
                ),
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onClick()
            }
            .background(
                if (isSelected)
                    Brush.horizontalGradient(
                        listOf(
                            Color(0xFF00E676).copy(alpha = 0.25f),
                            Color(0xFF00E676).copy(alpha = 0.10f)
                        )
                    )
                else Brush.horizontalGradient(
                    listOf(Color.Transparent, Color.Transparent)
                ),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(horizontal = 18.dp, vertical = 8.dp)
    ) {

        Icon(icon, contentDescription = null, tint = contentColor)

        Spacer(modifier = Modifier.height(4.dp))

        Text(label, color = contentColor, fontSize = 12.sp)
    }
}


@Composable
fun SectionTitle(title: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(title, color = Color.White, fontSize = 16.sp)
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .height(1.dp)
                .weight(1f)
                .background(Color(0xFF2A3345))
        )
    }
    Spacer(modifier = Modifier.height(14.dp))
}

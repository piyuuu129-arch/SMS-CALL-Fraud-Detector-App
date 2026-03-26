package com.example.frauddetectorapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Public
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.IntrinsicSize

// ================= ROOT SCREEN =================



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
                .shadow(20.dp, RoundedCornerShape(18.dp)) // 🔥 GLOW EFFECT
                .border(2.dp, Color(0xFF00FFAA), RoundedCornerShape(18.dp))
                .padding(horizontal = 32.dp, vertical = 18.dp)
        ) {
            Text(
                "LOGO",
                color = Color(0xFF00FFAA),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "SMART FRAUD SHIELD",
            color = Color.White,
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            "AI-POWERED CALL & SMS PROTECTION",
            color = Color(0xFF8A94A6),
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(36.dp))
    }
}

// ================= SECTION TITLE =================

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

// ================= CARD STYLE =================

//fun cardBrush() = Brush.verticalGradient(
//    listOf(
//        Color(0xFF0E1624),
//        Color(0xFF0B1320)
//    )
//)

// ================= INSIGHTS =================

@Composable
fun InsightsSection() {

    var pressed by remember { mutableStateOf(false) }

    Column {

        SectionTitle("Insights")

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .scale(if (pressed) 0.97f else 1f)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            pressed = true
                            tryAwaitRelease()
                            pressed = false
                        }
                    )
                },
            shape = RoundedCornerShape(15.dp), // 🔥 smoother corners
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {

            Box(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF0E1624),
                                Color(0xFF0B1A2D)
                            )
                        )
                    )
                    .padding(horizontal = 22.dp, vertical = 22.dp) // 🔥 increased padding
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column {

                        Text(
                            "DASHBOARD",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp // 🔥 bigger
                        )

                        Spacer(modifier = Modifier.height(6.dp)) // 🔥 more vertical gap

                        Text(
                            "View Insights",
                            color = Color(0xFF8A94A6),
                            fontSize = 14.sp // 🔥 slightly bigger
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForwardIos,
                        contentDescription = null,
                        tint = Color(0xFF8A94A6),
                        modifier = Modifier.size(18.dp) // 🔥 slightly bigger
                    )
                }
            }
        }
    }
}

// ================= MODE =================

@Composable
fun ModeSection() {

    var isEnabled by remember { mutableStateOf(true) }

    Column {

        SectionTitle("Modes")


        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            elevation = CardDefaults.cardElevation(0.dp)
        ) {

            Box(
                modifier = Modifier
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF0E1624),
                                Color(0xFF0B1A2D)
                            )
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 18.dp) // 🔥 tighter
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "DETECTION SUITE",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        val modeText = if (isEnabled) "Demo mode" else "Normal mode"

                        Text(
                            text = modeText,
                            color = Color(0xFF8A94A6),
                            fontSize = 14.sp
                        )
                    }

                    Switch(
                        checked = isEnabled,
                        onCheckedChange = { isEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Color.White,
                            checkedTrackColor = Color(0xFF00E676),
                            uncheckedTrackColor = Color(0xFF2A3345)
                        ),
                        modifier = Modifier.scale(0.9f) // 🔥 makes it compact
                    )
                }
            }
        }
    }
}

// ================= HISTORY =================

@Composable
fun MonitoringHistory() {

    Column {

        SectionTitle("Monitoring History")

        HistoryItem(
            "+1 (555) 012-9843",
            "Flagged for suspicious caller ID",
            "SPAM LIKELY",
            Color.Red,
            Icons.Default.Phone,
            "12:45 PM"
        )

        HistoryItem(
            "Amazon_Secure...",
            "Text contains urgent phishing link",
            "FRAUD DETECTED",
            Color(0xFFFF6A00),
            Icons.Default.Message,
            "10:12 AM"
        )

        HistoryItem(
            "secure-login-bank.io",
            "Deceptive site targeting credentials",
            "PHISHING",
            Color(0xFF9C27B0),
            Icons.Default.Public,
            "Yesterday"
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

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min) // 🔥 VERY IMPORTANT
        ) {

            // 🔥 LEFT COLOR STRIP (FIXED)
            Box(
                modifier = Modifier
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(
                        color,
                        RoundedCornerShape(
                            topStart = 18.dp,
                            bottomStart = 18.dp
                        )
                    )
            )

            // 🔥 MAIN CARD CONTENT
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color(0xFF0E1624),
                                Color(0xFF0B1A2D)
                            )
                        ),
                        RoundedCornerShape(18.dp)
                    )
                    .padding(18.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // 🔥 ICON BOX
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .background(
                                color.copy(alpha = 0.2f),
                                RoundedCornerShape(14.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = color,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // 🔥 TEXT
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {

                        Text(
                            title,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            subtitle,
                            color = Color(0xFF8A94A6),
                            fontSize = 12.sp
                        )
                    }

                    // 🔥 RIGHT SIDE
                    Column(
                        horizontalAlignment = Alignment.End
                    ) {

                        Box(
                            modifier = Modifier
                                .background(
                                    color.copy(alpha = 0.2f),
                                    RoundedCornerShape(50)
                                )
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                status,
                                color = color,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            time,
                            color = Color(0xFF8A94A6),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

// ================= BOTTOM NAV =================

@Composable
fun BottomNavBar() {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF060D1A))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        BottomItem("Home", true, Icons.Default.Home)
        BottomItem("Dashboard", false, Icons.Default.GridView)
        BottomItem("History", false, Icons.Default.History)
    }
}

@Composable
fun BottomItem(label: String, selected: Boolean, icon: ImageVector) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(
                if (selected) Color(0xFF0F2F25) else Color.Transparent,
                RoundedCornerShape(14.dp)
            )
            .padding(horizontal = 18.dp, vertical = 10.dp)
    ) {

        Icon(icon, contentDescription = null,
            tint = if (selected) Color(0xFF00E676) else Color.Gray)

        Text(
            label,
            color = if (selected) Color(0xFF00E676) else Color.Gray,
            fontSize = 12.sp
        )
    }
}
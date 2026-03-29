package com.example.frauddetectorapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frauddetectorapp.viewmodel.HomeViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically


@Composable
fun FraudHomeScreen() {

    // ✅ FIXED: use Screen instead of String
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

    val viewModel: HomeViewModel = viewModel()

    val latestCall = viewModel.latestCall.collectAsState().value
    val latestSms = viewModel.latestSms.collectAsState().value
    val latestUrl = viewModel.latestUrl.collectAsState().value

    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.loadSms(context)
        viewModel.loadCall(context)
        viewModel.loadUrl(context)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                AppBrush.BackgroundGradient
            )
    ) {

        when (currentScreen) {

            // ================= HOME =================
            is Screen.Home ->  AnimatedVisibility(
                visible = true,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 40 })
            )  {


                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 90.dp)
                ) {

                    Spacer(modifier = Modifier.height(10.dp))

                    HeaderSection()
                    Spacer(modifier = Modifier.height(20.dp))

                    InsightsSection(
                        onDashboardClick = {
                            currentScreen = Screen.Dashboard
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    ModeSection()
                    Spacer(modifier = Modifier.height(20.dp))

                    MonitoringHistory(
                        latestCall = latestCall,
                        latestSms = latestSms,
                        latestUrl = latestUrl
                    )
                }
            }

            // ================= DASHBOARD =================
            is Screen.Dashboard -> {

                BackHandler {
                    currentScreen = Screen.Home
                }

                DashboardScreen(
                    onHistoryClick = { currentScreen = Screen.History },
                    onSmsClick = { currentScreen = Screen.SmsAnalysis },
                    onCallClick = { currentScreen = Screen.CallAnalysis },
                    onHomeClick = { currentScreen = Screen.Home },
                    onScanClick = { currentScreen = Screen.Scan }
                )
            }

            // ================= SMS =================
            is Screen.SmsAnalysis -> {

                BackHandler {
                    currentScreen = Screen.Dashboard
                }

                SmsAnalysisScreen(
                    onBack = { currentScreen = Screen.Dashboard }
                )
            }

            // ✅ ADDED (minimal extension)
            is Screen.CallAnalysis -> {

                BackHandler {
                    currentScreen = Screen.Dashboard
                }

                CallAnalysisScreen(
                    onBack = { currentScreen = Screen.Dashboard }
                )
            }

            // ✅ ADDED (minimal extension)
            is Screen.History -> {

                BackHandler {
                    currentScreen = Screen.Dashboard
                }

                HistoryScreen(
                    onBack = { currentScreen = Screen.Dashboard },

                    onCallHistoryClick = {
                        currentScreen = Screen.CallHistory
                    },

                    onSmsHistoryClick = {
                        currentScreen = Screen.SmsHistory
                    },

                    onUrlHistoryClick = {
                        currentScreen = Screen.UrlHistory
                    },
                    onHomeClick = { currentScreen = Screen.Home },
                    onDashboardClick = { currentScreen = Screen.Dashboard },
                    onScanClick = { currentScreen = Screen.Scan }
                )
            }

            is Screen.Scan -> {
                BackHandler {
                    currentScreen = Screen.Home
                }
                ScanScreen()
            }

            else -> {}


        }

        // ✅ FIXED bottom nav
        Box(
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            BottomNavBar(
                selected = when (currentScreen) {
                    is Screen.Home -> "home"

                    is Screen.Dashboard,
                    is Screen.SmsAnalysis,
                    is Screen.CallAnalysis,
                    is Screen.CallHistory,
                    is Screen.SmsHistory,
                    is Screen.UrlHistory -> "dashboard"

                    is Screen.History -> "history"
                    is Screen.Scan -> "scan"
                },
                onHomeClick = { currentScreen = Screen.Home },
                onDashboardClick = { currentScreen = Screen.Dashboard },
                onHistoryClick = { currentScreen = Screen.History },
                onScanClick = { currentScreen = Screen.Scan }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FraudHomeScreenPreview() {
    FraudHomeScreen()
}



package com.example.frauddetectorapp

import com.example.frauddetectorapp.ui.FraudHomeScreen
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.*
import com.example.frauddetectorapp.ui.*
import com.example.frauddetectorapp.ui.Screen

class MainActivity : ComponentActivity() {

    private lateinit var modeLabel: TextView

    companion object {
        var demoMode = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ Compose UI
        setContent {

            var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

            when (currentScreen) {

                is Screen.Home -> {
                    FraudHomeScreen()
                }

                is Screen.Dashboard -> {
                    DashboardScreen(
                        onHistoryClick = {
                            currentScreen = Screen.History
                        },
                        onSmsClick = {
                            currentScreen = Screen.SmsAnalysis
                        },
                        onCallClick = {
                            currentScreen = Screen.CallAnalysis
                        },
                        onHomeClick = {
                            currentScreen = Screen.Home
                        }
                    )
                }

                is Screen.History -> {
                    HistoryScreen(
                        onBack = {
                            currentScreen = Screen.Dashboard
                        },
                        onCallHistoryClick = { },
                        onSmsHistoryClick = { },
                        onUrlHistoryClick = { }
                    )
                }

                is Screen.SmsAnalysis -> {
                    SmsAnalysisScreen(
                        onBack = {
                            currentScreen = Screen.Dashboard
                        }
                    )
                }

                is Screen.CallAnalysis -> {
                    CallAnalysisScreen(
                        onBack = {
                            currentScreen = Screen.Dashboard
                        }
                    )
                }
                else -> {} }
        }

        val detector = FraudDetector(this)
        val result = detector.analyze("Your SBI account will be blocked verify now")
        Log.d("FraudTest", "Prediction: $result")
        Log.e("APP_FLOW", "MainActivity Started")

        // ✅ Initialize ONNX model safely
        try {
            OnnxModelManager.init(this)
            ModelManager.getInterpreter(this)
            Log.e("ML_INIT", "ONNX Initialized")
        } catch (e: Exception) {
            Log.e("ML_INIT_ERROR", e.message ?: "Unknown error")
        }


        // ✅ Request runtime permissions
        requestAllPermissions()

        // ===========================
        // ⚠ FIX: SAFE VIEW HANDLING
        // ===========================



        val glow = findViewById<GlowView?>(R.id.glowView)
        glow?.setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        modeLabel = findViewById<TextView?>(R.id.modeLabel) ?: TextView(this)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        demoMode = prefs.getBoolean("demoMode", true)

        val modeSwitch = findViewById<SwitchCompat?>(R.id.modeSwitch)
        modeSwitch?.isChecked = demoMode

        updateModeText()

        modeSwitch?.setOnCheckedChangeListener { _, isChecked ->
            demoMode = isChecked
            prefs.edit().putBoolean("demoMode", demoMode).apply()
            updateModeText()
        }

        findViewById<LinearLayout?>(R.id.callHistoryBtn)?.setOnClickListener {
            startActivity(Intent(this, CallHistoryActivity::class.java))
        }

        findViewById<LinearLayout?>(R.id.smsHistoryBtn)?.setOnClickListener {
            startActivity(Intent(this, SmsHistoryActivity::class.java))
        }

        findViewById<LinearLayout?>(R.id.viewUrlHistoryBtn)?.setOnClickListener {
            startActivity(Intent(this, UrlHistoryActivity::class.java))
        }
    }

    private fun updateModeText() {
        if (::modeLabel.isInitialized) {
            modeLabel.text = if (demoMode) "Demo Mode" else "Normal Mode"
        }
    }

    // =========================================================
    // 🔥 Runtime Permission Handling
    // =========================================================

    private fun requestAllPermissions() {

        val permissions = mutableListOf(
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS
        )

        if (Build.VERSION.SDK_INT >= 33) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS)
        }

        val neededPermissions = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (neededPermissions.isNotEmpty()) {
            Log.e("PERMISSION", "Requesting permissions")
            ActivityCompat.requestPermissions(
                this,
                neededPermissions.toTypedArray(),
                111
            )
        } else {
            Log.e("PERMISSION", "All permissions already granted")
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 111) {
            permissions.forEachIndexed { index, permission ->
                if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("PERMISSION_GRANTED", permission)
                } else {
                    Log.e("PERMISSION_DENIED", permission)
                }
            }
        }
    }
}
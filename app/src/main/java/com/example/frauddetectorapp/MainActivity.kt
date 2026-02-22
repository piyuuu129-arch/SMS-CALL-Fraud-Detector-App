package com.example.frauddetectorapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var resultText: TextView
    private lateinit var modeLabel: TextView

    companion object {
        var demoMode = true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load ONNX safely
        try { OnnxModelManager.init(this) } catch (_: Exception) {}

        requestAllPermissions()

        resultText = findViewById(R.id.resultText)
        modeLabel = findViewById(R.id.modeLabel)

        val prefs = getSharedPreferences("settings", MODE_PRIVATE)
        demoMode = prefs.getBoolean("demoMode", true)

        val modeSwitch = findViewById<SwitchCompat>(R.id.modeSwitch)
        modeSwitch.isChecked = demoMode

        // 🔥 SET TEXT AT START
        updateModeText()

        // 🔥 WHEN SWITCH TOGGLED
        modeSwitch.setOnCheckedChangeListener { _, isChecked ->
            demoMode = isChecked
            prefs.edit().putBoolean("demoMode", demoMode).apply()
            updateModeText()
        }

        findViewById<Button>(R.id.callHistoryBtn).setOnClickListener {
            startActivity(Intent(this, CallHistoryActivity::class.java))
        }

        findViewById<Button>(R.id.smsHistoryBtn).setOnClickListener {
            startActivity(Intent(this, SmsHistoryActivity::class.java))
        }

        findViewById<Button>(R.id.viewUrlHistoryBtn).setOnClickListener {
            startActivity(Intent(this, UrlHistoryActivity::class.java))
        }
    }

    private fun updateModeText() {
        if (demoMode) {
            modeLabel.text = "Demo Mode"
            resultText.text = " Protection Active"
        } else {
            modeLabel.text = "Normal Mode"
            resultText.text = " Protection Active"
        }
    }

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

        val needed = permissions.filter {
            ContextCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED
        }

        if (needed.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, needed.toTypedArray(), 111)
        }
    }
}
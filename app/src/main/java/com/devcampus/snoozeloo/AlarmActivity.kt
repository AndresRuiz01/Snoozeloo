package com.devcampus.snoozeloo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.devcampus.snoozeloo.alarm.presentation.trigger.AlarmTriggerScreenRoot

class AlarmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setShowWhenLocked(true)
        setTurnScreenOn(true)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        hideSystemBars()

        val alarmId = intent.getLongExtra("ALARM_ID", -1)

        // no alarm found
        if (alarmId == -1L) {
            cleanup()
        }

        setContent {
            AlarmTriggerScreenRoot(1)
        }
    }

    private fun hideSystemBars() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }

    private fun cleanup() {
        finish()
    }

}
package com.devcampus.snoozeloo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.devcampus.snoozeloo.ui.theme.SnoozelooTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SnoozelooTheme {
                NavigationRoot()
            }
        }
    }
}
package com.devcampus.snoozeloo.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val colorScheme = lightColorScheme(
    primary = SnoozelooBlue,
    primaryContainer = SnoozelooBlue,
    background = SnoozelooBackground
)

@Composable
fun SnoozelooTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
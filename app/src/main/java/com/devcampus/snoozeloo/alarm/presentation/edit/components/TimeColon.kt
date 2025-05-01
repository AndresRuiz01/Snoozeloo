package com.devcampus.snoozeloo.alarm.presentation.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TimeColon() {
    Column(
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(RoundedCornerShape(100f))
                .background(Color(0xFF858585))
        )
        Box(
            modifier = Modifier
                .size(4.dp)
                .clip(RoundedCornerShape(100f))
                .background(Color(0xFF858585))
        )
    }
}
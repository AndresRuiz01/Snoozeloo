package com.devcampus.snoozeloo.alarm.presentation.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devcampus.snoozeloo.ui.theme.montserrat

@Composable
fun SliderCell(
    title: String,
    value: Float,
    onValueChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color(0xFF0D0F19),
                fontSize = 16.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.SemiBold
            )
        )

        Slider(
            value = value,
            onValueChange = onValueChanged,
            valueRange = 0f..100f, // Range from 0 to 100
            steps = 99,
            colors = SliderDefaults.colors().copy(
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
                inactiveTrackColor = Color(0xFFECEFFF)
            )
        )
    }
}
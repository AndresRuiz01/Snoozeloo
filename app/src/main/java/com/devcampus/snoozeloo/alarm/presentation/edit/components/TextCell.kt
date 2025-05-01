package com.devcampus.snoozeloo.alarm.presentation.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devcampus.snoozeloo.ui.theme.montserrat

@Composable
fun TextCell(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable {
                onClick()
            }
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
        Text(
            text = subtitle,
            style = TextStyle(
                color = Color(0xFF858585),
                fontSize = 14.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

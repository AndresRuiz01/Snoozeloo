package com.devcampus.snoozeloo.alarm.presentation.list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devcampus.snoozeloo.R
import com.devcampus.snoozeloo.ui.theme.montserrat

@Composable
fun NoAlarmCell(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(32.dp),
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.alarm),
                tint = Color(0xFF4664FF),
                contentDescription = null
            )
            Text(
                text = "It's empty! Add the first alarm so you\n" +
                        "don't miss an important moment!",
                color = Color(0xFF0D0F19),
                style = TextStyle(
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = montserrat,
                    fontWeight = FontWeight.Medium
                ),
            )
        }
    }
}
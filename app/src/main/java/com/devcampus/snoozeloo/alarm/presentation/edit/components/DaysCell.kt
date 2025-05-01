package com.devcampus.snoozeloo.alarm.presentation.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devcampus.snoozeloo.alarm.presentation.components.DayCell
import com.devcampus.snoozeloo.ui.theme.montserrat
import java.time.DayOfWeek

@Composable
fun DaysCell(
    title: String,
    days: List<DayOfWeek>,
    onDayClicked: (DayOfWeek) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
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
        DayCell(
            days = days,
            onDayClicked = onDayClicked
        )
    }
}
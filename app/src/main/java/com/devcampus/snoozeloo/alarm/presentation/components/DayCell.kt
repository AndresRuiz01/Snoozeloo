package com.devcampus.snoozeloo.alarm.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.devcampus.snoozeloo.ui.theme.SnoozelooBlue
import java.time.DayOfWeek
import java.util.Locale

private val dayOptions = listOf(
    DayOfWeek.MONDAY,
    DayOfWeek.TUESDAY,
    DayOfWeek.WEDNESDAY,
    DayOfWeek.THURSDAY,
    DayOfWeek.FRIDAY,
    DayOfWeek.SATURDAY,
    DayOfWeek.SUNDAY
)

@Composable
fun DayCell(
    days: List<DayOfWeek>,
    onDayClicked: (DayOfWeek) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        dayOptions.forEach {
            val dayEnabled = it in days
            Box(
                modifier = Modifier
                    .height(26.dp)
                    .clip(RoundedCornerShape(100f))
                    .background(
                        if (dayEnabled) {
                            SnoozelooBlue
                        } else {
                            Color(0xFFECEFFF)
                        }
                    )
                    .weight(1f)
                    .clickable {
                        onDayClicked(it)
                    }
            ) {
                Text(
                    text = it.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()).take(2),
                    color = if (dayEnabled) {
                        Color.White
                    } else {
                        Color(0xFF0D0F19)
                    },
                    modifier = Modifier.fillMaxSize().wrapContentSize()
                )
            }
        }
    }
}
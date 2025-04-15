package com.devcampus.snoozeloo.alarm.presentation.list.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devcampus.snoozeloo.alarm.presentation.components.DayCell
import com.devcampus.snoozeloo.ui.theme.SnoozelooTheme
import com.devcampus.snoozeloo.ui.theme.montserrat
import java.time.DayOfWeek
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

@Composable
fun AlarmCell(
    title: String,
    time: LocalTime,
    enabled: Boolean,
    onEnabledChanged: (Boolean) -> Unit,
    days: List<DayOfWeek>,
    onAlarmClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable {
                onAlarmClicked()
            }
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    val hourMinuteFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm", Locale.getDefault())
                    val amPmFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("a", Locale.getDefault())

                    Text(
                        text = time.format(hourMinuteFormatter),
                        style = TextStyle(
                            color = Color(0xFF0D0F19),
                            fontSize = 42.sp,
                            lineHeight = 42.sp,
                            fontFamily = montserrat,
                            fontWeight = FontWeight.Medium
                        ),
                    )
                    Text(
                        text = time.format(amPmFormatter),
                        style = TextStyle(
                            color = Color(0xFF0D0F19),
                            fontSize = 24.sp,
                            lineHeight = 24.sp,
                            fontFamily = montserrat,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .padding(bottom = 6.dp)
                    )
                }
                val until = formatAlarmInFuture(time)
                Text(
                    text = until,
                    style = TextStyle(
                        color = Color(0xFF858585),
                        fontSize = 14.sp,
                        lineHeight = 14.sp,
                        fontFamily = montserrat,
                        fontWeight = FontWeight.Medium,
                    ),
                )
            }
            Switch(
                checked = enabled,
                onCheckedChange = onEnabledChanged,
                colors = SwitchDefaults.colors().copy(
                    uncheckedTrackColor = Color(0xFFBCC6FF),
                    uncheckedThumbColor = Color.White,
                    uncheckedBorderColor = Color.White,
                ),
            )
        }
        DayCell(
            days = days,
            onDayClicked = { }
        )
        Text(
            text = getBedtimeString(time),
            style = TextStyle(
                color = Color(0xFF858585),
                fontSize = 14.sp,
                lineHeight = 14.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.Medium,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AlarmCellPreview() {
    SnoozelooTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF6F6F6))
                .padding(horizontal = 16.dp)
        ) {
            Spacer(Modifier)
            AlarmCell(
                title = "Wake Up",
                time = LocalTime.of(10, 0),
                enabled = true,
                days = listOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY,
                    DayOfWeek.FRIDAY
                ),
                onEnabledChanged = { },
                onAlarmClicked = { }
            )
            AlarmCell(
                title = "Education",
                time = LocalTime.of(16, 30),
                enabled = true,
                days = listOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.FRIDAY
                ),
                onEnabledChanged = { },
                onAlarmClicked = { }
            )
            AlarmCell(
                title = "Dinner",
                time = LocalTime.of(18, 0),
                enabled = false,
                days = listOf(
                    DayOfWeek.MONDAY,
                    DayOfWeek.TUESDAY,
                    DayOfWeek.WEDNESDAY,
                    DayOfWeek.THURSDAY,
                    DayOfWeek.FRIDAY,
                    DayOfWeek.SATURDAY,
                    DayOfWeek.SUNDAY,
                ),
                onEnabledChanged = { },
                onAlarmClicked = { }
            )
        }
    }
}

fun getBedtimeString(alarmTime: LocalTime): String {
    val bedtime = alarmTime.minusHours(8)
    val formatter = DateTimeFormatter.ofPattern("hh:mma", Locale.US) // Use Locale.US for AM/PM

    return "Go to bed at ${bedtime.format(formatter)} to get 8h of sleep"
}

fun formatAlarmInFuture(targetTime: LocalTime): String {
    val now = LocalTime.now()
    var hours = ChronoUnit.HOURS.between(now, targetTime)
    var minutes = ChronoUnit.MINUTES.between(now, targetTime) % 60

    if (hours < 0) {
        hours += 24 // Account for crossing midnight
    }

    if (minutes < 0) {
        minutes += 60 // Account for crossing midnight
    }

    val parts = mutableListOf<String>()

    if (hours > 0) {
        parts.add("${hours}h")
    }
    if (minutes > 0) {
        parts.add("${minutes}min")
    }

    return if (parts.isEmpty()) {
        "Alarm is now"
    } else {
        "Alarm in ${parts.joinToString(" ")}"
    }
}
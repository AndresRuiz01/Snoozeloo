package com.devcampus.snoozeloo.alarm.domain.models

import java.time.DayOfWeek
import java.time.LocalTime

data class Alarm(
    val alarmId: Long,
    val title: String,
    val time: LocalTime,
    val days: List<DayOfWeek>,
    val volume: Int,
    val vibrate: Boolean,
    val enabled: Boolean,
) {
    companion object {
        val defaultAlarm = Alarm(
            alarmId = 0,
            title = "",
            time = LocalTime.of(0,0),
            days = listOf(),
            volume = 100,
            vibrate = true,
            enabled = true
        )
    }
}
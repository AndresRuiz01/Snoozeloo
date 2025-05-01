package com.devcampus.snoozeloo.alarm.domain.models

import com.devcampus.snoozeloo.ringtone.domain.Ringtone
import com.devcampus.snoozeloo.ringtone.domain.silentRingtone
import java.time.DayOfWeek
import java.time.LocalTime

data class Alarm(
    val alarmId: Long,
    val title: String,
    val time: LocalTime,
    val days: List<DayOfWeek>,
    val volume: Int,
    val ringtone: Ringtone,
    val vibrate: Boolean,
    val enabled: Boolean,
) {
    val isRepeating = days.isNotEmpty()

    companion object {
        val defaultAlarm = Alarm(
            alarmId = 0,
            title = "",
            time = LocalTime.of(0,0),
            days = listOf(),
            volume = 100,
            vibrate = true,
            ringtone = silentRingtone,
            enabled = true
        )
    }
}
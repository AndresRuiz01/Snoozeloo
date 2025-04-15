package com.devcampus.snoozeloo.alarm.presentation.edit

import com.devcampus.snoozeloo.alarm.domain.models.Alarm

data class EditAlarmState(
    val originalAlarm: Alarm? = null,
    val alarm: Alarm? = null,
) {
    val isSaveEnabled: Boolean get() {
        return originalAlarm != alarm
    }
}
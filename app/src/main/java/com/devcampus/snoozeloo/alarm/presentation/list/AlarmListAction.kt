package com.devcampus.snoozeloo.alarm.presentation.list

import java.time.DayOfWeek

sealed interface AlarmListAction {
    data class ToggleAlarm(val id: Long): AlarmListAction
    data class ToggleDay(val id: Long, val day: DayOfWeek): AlarmListAction
}
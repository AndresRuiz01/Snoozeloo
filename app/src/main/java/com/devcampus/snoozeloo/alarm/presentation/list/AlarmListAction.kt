package com.devcampus.snoozeloo.alarm.presentation.list

sealed interface AlarmListAction {
    data class ToggleAlarm(val id: Long): AlarmListAction
}
package com.devcampus.snoozeloo.alarm.presentation.edit

import java.time.DayOfWeek

sealed interface EditAlarmAction {
    data class ToggleVibrate(val enabled: Boolean): EditAlarmAction
    data class DayClicked(val day: DayOfWeek): EditAlarmAction
    data class OnVolumeChanged(val volume: Int): EditAlarmAction
    data object OnSave: EditAlarmAction
}
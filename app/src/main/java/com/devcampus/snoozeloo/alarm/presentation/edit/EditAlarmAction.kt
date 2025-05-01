package com.devcampus.snoozeloo.alarm.presentation.edit

import com.devcampus.snoozeloo.ringtone.domain.Ringtone
import java.time.DayOfWeek
import java.time.LocalTime

sealed interface EditAlarmAction {
    data class ToggleVibrate(val enabled: Boolean): EditAlarmAction
    data class DayClicked(val day: DayOfWeek): EditAlarmAction
    data class OnVolumeChanged(val volume: Int): EditAlarmAction
    data class OnRingtoneChanged(val ringtone: Ringtone): EditAlarmAction
    data object OnSave: EditAlarmAction
    data class OnSaveTitle(val title: String): EditAlarmAction
    data object ShowEditTitle: EditAlarmAction
    data object HideEditTitle: EditAlarmAction
    data class OnTimeChanged(val time: LocalTime): EditAlarmAction
    data object ShowTimePicker: EditAlarmAction
    data object HideTimePicker: EditAlarmAction
}
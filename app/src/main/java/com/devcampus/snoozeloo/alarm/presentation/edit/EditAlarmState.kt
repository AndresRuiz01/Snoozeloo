package com.devcampus.snoozeloo.alarm.presentation.edit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import com.devcampus.snoozeloo.alarm.domain.models.Alarm

@OptIn(ExperimentalMaterial3Api::class)
data class EditAlarmState(
    val alarm: Alarm? = null,
    val showEditTitle: Boolean = false,
    val showTimePicker: Boolean = false,
    val timePickerState: TimePickerState = TimePickerState(0, 0, false)
)
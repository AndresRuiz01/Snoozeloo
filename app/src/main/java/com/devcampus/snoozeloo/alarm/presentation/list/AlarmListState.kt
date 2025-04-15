package com.devcampus.snoozeloo.alarm.presentation.list

import com.devcampus.snoozeloo.alarm.domain.models.Alarm

data class AlarmListState(
    val alarms: List<Alarm> = listOf()
)
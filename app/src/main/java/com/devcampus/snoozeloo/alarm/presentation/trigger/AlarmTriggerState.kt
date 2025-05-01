package com.devcampus.snoozeloo.alarm.presentation.trigger

import com.devcampus.snoozeloo.alarm.domain.models.Alarm

data class AlarmTriggerState(
    val alarm: Alarm? = null
)
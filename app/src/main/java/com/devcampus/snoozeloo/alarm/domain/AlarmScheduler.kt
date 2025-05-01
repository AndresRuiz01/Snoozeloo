package com.devcampus.snoozeloo.alarm.domain

import com.devcampus.snoozeloo.alarm.domain.models.Alarm

const val EXTRA_ALARM_ID = "com.snoozeloo.alarm.EXTRA_ALARM_ID"

interface AlarmScheduler {
    fun scheduleAlarm(alarm: Alarm)
}
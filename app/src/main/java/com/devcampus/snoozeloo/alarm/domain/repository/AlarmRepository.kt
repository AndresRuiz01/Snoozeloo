package com.devcampus.snoozeloo.alarm.domain.repository

import com.devcampus.snoozeloo.alarm.domain.models.Alarm
import kotlinx.coroutines.flow.Flow

interface AlarmRepository {
    suspend fun getAlarm(id: Long): Alarm?
    suspend fun getAlarmsFlow(): Flow<List<Alarm>>
    suspend fun getAlarms(): List<Alarm>
    suspend fun upsertAlarm(alarm: Alarm): Long
}
package com.devcampus.snoozeloo.alarm.data.repository

import com.devcampus.snoozeloo.alarm.database.dao.AlarmDao
import com.devcampus.snoozeloo.alarm.database.utils.toAlarm
import com.devcampus.snoozeloo.alarm.database.utils.toAlarmEntity
import com.devcampus.snoozeloo.alarm.domain.models.Alarm
import com.devcampus.snoozeloo.alarm.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class AlarmRepositoryImpl(
    private val alarmDao: AlarmDao
) : AlarmRepository {

    override suspend fun getAlarm(id: Long): Alarm? {
        return alarmDao.getAlarm(id)?.toAlarm()
    }

    override suspend fun getAlarmsFlow(): Flow<List<Alarm>> {
        return alarmDao.getAlarmsFlow().map { it.map { it.toAlarm() } }
    }

    override suspend fun getAlarms(): List<Alarm> {
        return alarmDao.getAlarms().map { it.toAlarm() }
    }

    override suspend fun upsertAlarm(alarm: Alarm): Long {
        return alarmDao.upsertAlarm(alarm.toAlarmEntity())
    }
}
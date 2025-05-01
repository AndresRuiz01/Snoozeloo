package com.devcampus.snoozeloo.alarm.data.use_case

import com.devcampus.snoozeloo.alarm.domain.AlarmScheduler
import com.devcampus.snoozeloo.alarm.domain.models.Alarm
import com.devcampus.snoozeloo.alarm.domain.repository.AlarmRepository
import com.devcampus.snoozeloo.alarm.domain.use_case.UpsertAlarmUseCase

/*
 * Any consuming Alarm screen should use this use case to update or create alarms
 * This ensure that when an alarm is updated, it is rescheduled for the correct time and day
 */
class UpsertAlarmUseCaseImpl(
    private val alarmRepository: AlarmRepository,
    private val alarmScheduler: AlarmScheduler
) : UpsertAlarmUseCase {
    override suspend operator fun invoke(alarm: Alarm) {
        val id = alarmRepository.upsertAlarm(alarm)
        if (alarm.alarmId == 0L) {
            alarmScheduler.scheduleAlarm(alarm.copy(alarmId = id))
        } else {
            alarmScheduler.scheduleAlarm(alarm)
        }
    }
}
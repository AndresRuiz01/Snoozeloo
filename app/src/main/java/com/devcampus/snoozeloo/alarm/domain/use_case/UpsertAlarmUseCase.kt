package com.devcampus.snoozeloo.alarm.domain.use_case

import com.devcampus.snoozeloo.alarm.domain.models.Alarm


interface UpsertAlarmUseCase {
    suspend operator fun invoke(alarm: Alarm)
}
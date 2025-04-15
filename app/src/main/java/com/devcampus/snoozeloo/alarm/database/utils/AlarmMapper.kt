package com.devcampus.snoozeloo.alarm.database.utils

import com.devcampus.snoozeloo.alarm.database.entity.AlarmEntity
import com.devcampus.snoozeloo.alarm.domain.models.Alarm

fun AlarmEntity.toAlarm(): Alarm {
    return Alarm(
        alarmId = this.alarmId,
        title = this.title,
        time = this.time,
        days = this.days,
        volume = this.volume,
        vibrate = this.vibrate,
        enabled = this.enabled
    )
}

fun Alarm.toAlarmEntity(): AlarmEntity {
    return AlarmEntity(
        alarmId = this.alarmId,
        title = this.title,
        time = this.time,
        days = this.days,
        volume = this.volume,
        vibrate = this.vibrate,
        enabled = this.enabled
    )
}
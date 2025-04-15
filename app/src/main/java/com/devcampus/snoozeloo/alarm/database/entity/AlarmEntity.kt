package com.devcampus.snoozeloo.alarm.database.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.devcampus.snoozeloo.alarm.database.utils.AlarmConverters
import java.time.DayOfWeek
import java.time.LocalTime

@Entity(
    tableName = "alarm",
    indices = [Index(value = ["alarmId"])]
)
@TypeConverters(AlarmConverters::class)
data class AlarmEntity(
    @PrimaryKey(autoGenerate = true) val alarmId: Long,
    val title: String,
    val time: LocalTime,
    val days: List<DayOfWeek>,
    val volume: Int,
    val vibrate: Boolean,
    val enabled: Boolean,
)
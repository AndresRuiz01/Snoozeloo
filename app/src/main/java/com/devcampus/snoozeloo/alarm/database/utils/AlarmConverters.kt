package com.devcampus.snoozeloo.alarm.database.utils

import androidx.room.TypeConverter
import java.time.DayOfWeek
import java.time.LocalTime

class AlarmConverters {

    @TypeConverter
    fun fromLocalTime(value: LocalTime): String {
        return "${value.hour}:${value.minute}"
    }
    @TypeConverter
    fun toLocalTime(value: String): LocalTime {
        val hourMin = value.split(":")
        return LocalTime.of(hourMin[0].toInt(), hourMin[1].toInt())
    }

    @TypeConverter
    fun fromDaysOfWeek(value: List<DayOfWeek>): String {
        return value.joinToString(separator = ",") {
            it.name
        }
    }

    @TypeConverter
    fun toDaysOfWeek(value: String): List<DayOfWeek> {
        if (value.isEmpty()) {
            return listOf()
        }
        return value.split(",").map {
            DayOfWeek.valueOf(it)
        }
    }

}
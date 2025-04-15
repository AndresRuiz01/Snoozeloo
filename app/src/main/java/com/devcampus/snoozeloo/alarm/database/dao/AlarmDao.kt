package com.devcampus.snoozeloo.alarm.database.dao

import androidx.room.Dao
import androidx.room.Database
import androidx.room.Query
import androidx.room.RoomDatabase
import androidx.room.Upsert
import com.devcampus.snoozeloo.alarm.database.entity.AlarmEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AlarmDao {

    @Upsert
    suspend fun upsertAlarm(alarmEntity: AlarmEntity): Long

    @Query("SELECT * FROM alarm WHERE alarmId=:alarmId")
    suspend fun getAlarm(alarmId: Long): AlarmEntity?

    @Query("SELECT * FROM alarm")
    fun getAlarmsFlow(): Flow<List<AlarmEntity>>

    @Query("SELECT * FROM alarm")
    fun getAlarms(): List<AlarmEntity>

}


@Database(
    entities = [
        AlarmEntity::class,
    ],
    version = 2
)
abstract class SnoozelooDatabase : RoomDatabase() {
    abstract val alarmDao: AlarmDao
}
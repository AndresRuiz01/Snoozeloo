package com.devcampus.snoozeloo.alarm.di

import androidx.room.Room
import androidx.room.migration.Migration
import com.devcampus.snoozeloo.alarm.data.repository.AlarmRepositoryImpl
import com.devcampus.snoozeloo.alarm.database.dao.SnoozelooDatabase
import com.devcampus.snoozeloo.alarm.domain.repository.AlarmRepository
import com.devcampus.snoozeloo.alarm.presentation.edit.EditAlarmViewModel
import com.devcampus.snoozeloo.alarm.presentation.list.AlarmListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


private val migration = Migration(1,2 ) {

}

val alarmModule = module {

    single {
        val dbName = "snoozeloo.db"
        val builder = Room.databaseBuilder(
            androidApplication(),
            SnoozelooDatabase::class.java,
            dbName
        )
        builder.addMigrations(migration)
        builder.build()
    }

    single { get<SnoozelooDatabase>().alarmDao }
    single<AlarmRepository> { AlarmRepositoryImpl(get()) }
    viewModel { AlarmListViewModel(get()) }
    viewModel { (alarmId: Long?) ->
        EditAlarmViewModel(get(), alarmId)
    }


}

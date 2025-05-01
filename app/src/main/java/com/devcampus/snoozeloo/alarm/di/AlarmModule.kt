package com.devcampus.snoozeloo.alarm.di

import androidx.room.Room
import com.devcampus.snoozeloo.alarm.presentation.util.AndroidAlarmScheduler
import com.devcampus.snoozeloo.alarm.data.repository.AlarmRepositoryImpl
import com.devcampus.snoozeloo.alarm.data.use_case.UpsertAlarmUseCaseImpl
import com.devcampus.snoozeloo.alarm.database.dao.SnoozelooDatabase
import com.devcampus.snoozeloo.alarm.domain.AlarmScheduler
import com.devcampus.snoozeloo.alarm.domain.repository.AlarmRepository
import com.devcampus.snoozeloo.alarm.domain.use_case.UpsertAlarmUseCase
import com.devcampus.snoozeloo.alarm.presentation.edit.EditAlarmViewModel
import com.devcampus.snoozeloo.alarm.presentation.list.AlarmListViewModel
import com.devcampus.snoozeloo.alarm.presentation.trigger.AlarmTriggerViewModel
import com.devcampus.snoozeloo.ringtone.data.AndroidRingtonePlayer
import com.devcampus.snoozeloo.ringtone.data.AndroidRingtoneRepository
import com.devcampus.snoozeloo.ringtone.domain.Ringtone
import com.devcampus.snoozeloo.ringtone.domain.RingtonePlayer
import com.devcampus.snoozeloo.ringtone.domain.RingtoneRepository
import com.devcampus.snoozeloo.ringtone.presentation.list.RingtoneListViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val alarmModule = module {
    // DB
    single {
        val dbName = "snoozeloo_3.db"
        val builder = Room.databaseBuilder(
            androidApplication(),
            SnoozelooDatabase::class.java,
            dbName
        )
        builder.build()
    }

    single { get<SnoozelooDatabase>().alarmDao }

    // Repositories
    single<AlarmRepository> { AlarmRepositoryImpl(get()) }
    single<RingtoneRepository> { AndroidRingtoneRepository(androidContext()) }
    single<RingtonePlayer> { AndroidRingtonePlayer(androidContext()) }
    single<AlarmScheduler> { AndroidAlarmScheduler(androidContext()) }
    single<UpsertAlarmUseCase> { UpsertAlarmUseCaseImpl(get(), get()) }

    // ViewModels
    viewModel { AlarmListViewModel(get(), get()) }
    viewModel { (alarmId: Long?) -> EditAlarmViewModel(get(), get(), get(), alarmId) }
    viewModel { (ringtone: Ringtone) -> RingtoneListViewModel(get(), get(), ringtone) }
    viewModel { (alarmId: Long) -> AlarmTriggerViewModel(get(), alarmId)  }
}

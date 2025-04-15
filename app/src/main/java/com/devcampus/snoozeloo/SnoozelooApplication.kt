package com.devcampus.snoozeloo

import android.app.Application
import com.devcampus.snoozeloo.alarm.di.alarmModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin


class SnoozelooApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@SnoozelooApplication)
             modules(alarmModule)
        }
    }
}
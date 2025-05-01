package com.devcampus.snoozeloo.alarm.presentation.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.devcampus.snoozeloo.alarm.domain.AlarmScheduler
import com.devcampus.snoozeloo.alarm.domain.EXTRA_ALARM_ID
import com.devcampus.snoozeloo.alarm.domain.repository.AlarmRepository
import com.devcampus.snoozeloo.alarm.domain.use_case.UpsertAlarmUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent

class AlarmReceiver : BroadcastReceiver() {

    // inject needed alarm classes
    private val alarmRepository: AlarmRepository by KoinJavaComponent.inject(AlarmRepository::class.java)
    private val alarmScheduler: AlarmScheduler by KoinJavaComponent.inject(AlarmScheduler::class.java)
    private val upsertAlarmUseCase: UpsertAlarmUseCase by KoinJavaComponent.inject(
        UpsertAlarmUseCase::class.java)

    // scope for alarm receiver tasks
    private val receiverScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onReceive(context: Context, intent: Intent) {
        // give receiver extra time if needed
        val pendingResult = goAsync()
        val alarmId = intent.getLongExtra(EXTRA_ALARM_ID, -1L)
        Log.d("AlarmReceiver", "Alarm triggered: ID")

        // Alternative: Extract ID from intent.data if using Uri matching
        // val alarmId = intent.data?.lastPathSegment?.toLongOrNull() ?: -1L

        receiverScope.launch(Dispatchers.IO) {
            try {
                if (alarmId != -1L) {

                    // get the alarm details
                    alarmRepository.getAlarm(alarmId)?.let { alarm ->
                        // play sound, vibrate, set volume accordingly

                        // if it's a repeating alarm, we need to schedule the next one
                        // otherwise we should disable the alarm
                        if (alarm.isRepeating) {
                            // schedule next alarm
                            alarmScheduler.scheduleAlarm(alarm)
                        } else {
                            // turn off alarm
                            upsertAlarmUseCase(
                                alarm = alarm.copy(
                                    enabled = false
                                ),
                            )
                        }
                    }
                }
            } catch (ex: Exception) {
                Log.e("AlarmReceiver", "Error processing alarm $alarmId", ex)
            } finally {
                pendingResult.finish()
            }
        }
    }
}


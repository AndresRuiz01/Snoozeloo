package com.devcampus.snoozeloo.alarm.presentation.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import com.devcampus.snoozeloo.alarm.domain.AlarmScheduler
import com.devcampus.snoozeloo.alarm.domain.EXTRA_ALARM_ID
import com.devcampus.snoozeloo.alarm.domain.models.Alarm
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date

class AndroidAlarmScheduler(
    private val context: Context
) : AlarmScheduler {

    companion object {
        private const val ALARM_ACTION = "com.snoozeloo.alarm.ALARM_TRIGGER"
        private const val ALARM_SCHEME = "snoozeloo"
        private const val ALARM_AUTHORITY = "alarm"
    }

    override fun scheduleAlarm(alarm: Alarm) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // Create the unique PendingIntent for this alarm
        val pendingIntent = createPendingIntent(alarm.alarmId)

        // If alarm is disabled, cancel any existing schedule and return
        if (!alarm.enabled) {
            Log.d("AlarmScheduler", "Alarm ${alarm.alarmId} is disabled. Cancelling any existing schedule.")
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel() // Also cancel the PI itself
            return
        }

        // Calculate the next trigger time
        val nextTriggerMillis = calculateNextTriggerTimeMillis(alarm)

        if (nextTriggerMillis != null) {
            // Check for permission (essential for Android 12+)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                if (!alarmManager.canScheduleExactAlarms()) {
                    Log.e("AlarmScheduler", "Cannot schedule exact alarms. Missing permission (SCHEDULE_EXACT_ALARM / USE_EXACT_ALARM). Alarm ${alarm.alarmId} will not be scheduled.")
                    // TODO: Inform the user or request permission.
                    // Optionally cancel just in case something was scheduled before permission revoked
                    alarmManager.cancel(pendingIntent)
                    pendingIntent.cancel()
                    return
                }
            }

            Log.d("AlarmScheduler", "Scheduling alarm ${alarm.alarmId} for: ${Date(nextTriggerMillis)} (Timestamp: $nextTriggerMillis)")

            // Schedule the exact alarm
            // setExactAndAllowWhileIdle is needed to wake the device even in Doze mode
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP, // Use wall clock time, wake device
                nextTriggerMillis,
                pendingIntent
            )
        } else {
            // No valid future time found (e.g., non-repeating alarm in the past)
            // Cancel any potentially existing schedule for this ID just in case.
            Log.d("AlarmScheduler", "No valid future time for alarm ${alarm.alarmId}. Cancelling any existing schedule.")
            alarmManager.cancel(pendingIntent)
            pendingIntent.cancel()
        }
    }

    private fun createPendingIntent(alarmId: Long): PendingIntent {
        val appContext = context.applicationContext
        val intent = Intent(appContext, AlarmReceiver::class.java).apply {
            action = ALARM_ACTION
            data = Uri.parse("$ALARM_SCHEME://$ALARM_AUTHORITY/$alarmId")
            putExtra(EXTRA_ALARM_ID, alarmId) // include alarm ID to know which one is going off
        }

        // create a  pending intent with a unique request code (the specific alarm id)
        // additionally add update current flag to allow intents to be updated
        // if they currently exist
        return PendingIntent.getBroadcast(
            appContext,
            alarmId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun calculateNextTriggerTimeMillis(alarm: Alarm): Long? {
        val now = ZonedDateTime.now(ZoneId.systemDefault())
        val alarmTime = alarm.time

        if (alarm.days.isEmpty()) {
            // Non-repeating alarm
            val potentialTriggerToday = now.with(alarmTime)
            return if (potentialTriggerToday.isAfter(now)) {
                // Alarm time today is in the future
                potentialTriggerToday.toInstant().toEpochMilli()
            } else {
                // Alarm time today has passed. A non-repeating alarm set for a past time
                // we need to schedule it for the next day
                val potentialTriggerTomorrow = now.plusDays(1).with(alarmTime)
                potentialTriggerTomorrow.toInstant().toEpochMilli()
            }
        } else {
            // Repeating alarm
            var nextTrigger: ZonedDateTime? = null
            val sortedDays = alarm.days.sorted() // Ensure consistent order if needed

            // Start checking from today
            var currentDay = now.dayOfWeek
            var daysToAdd: Long = 0

            // Iterate up to 7 days to find the next valid day
            for (i in 0..7) {
                val checkDay = currentDay.plus(daysToAdd)
                if (sortedDays.contains(checkDay)) {
                    // Found a valid day
                    val potentialTrigger = now.plusDays(daysToAdd).with(alarmTime)

                    // If it's today AND the time has already passed, continue to the next valid day
                    if (daysToAdd == 0L && potentialTrigger.isBefore(now)) {
                        // Today's time passed, check next occurrence
                    } else {
                        // Found the next trigger time (either today in the future, or a future day)
                        nextTrigger = potentialTrigger
                        break // Exit loop once the first future occurrence is found
                    }
                }
                // Move to the next day for the next iteration
                daysToAdd++
            }

            return nextTrigger?.toInstant()?.toEpochMilli()
        }
    }
}
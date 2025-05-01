package com.devcampus.snoozeloo.alarm.presentation.util

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.devcampus.snoozeloo.AlarmActivity
import com.devcampus.snoozeloo.R
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

    fun test(context: Context, alarmId: Long) {
        val fullScreenIntent = Intent(context, AlarmActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or // Needed when starting from Receiver
                    Intent.FLAG_ACTIVITY_CLEAR_TASK or // Clears existing task
                    Intent.FLAG_ACTIVITY_NO_ANIMATION // Optional: No transition animation
            putExtra("ALARM_ID", alarmId) // Pass data
        }

//        val notificationManager = ContextCompat.getSystemService(context, NotificationManager::class.java) as NotificationManager
//        val channelId = "alarm_channel" // Ensure this channel exists!
//
//        // Create notification channel (do this once at app startup)
//        val channel = NotificationChannel(channelId, "Alarms", NotificationManager.IMPORTANCE_HIGH)
//            .apply { description = "Alarm notifications"
//                setSound(null, null) // Handle sound separately if needed
//            }
//        notificationManager.createNotificationChannel(channel)
//
//        val fullScreenPendingIntent = PendingIntent.getActivity(context,
//            123, // Unique request code
//            fullScreenIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
//
//        val builder = NotificationCompat.Builder(context, channelId)
//            .setSmallIcon(R.drawable.clock) // Replace with your icon
//            .setContentTitle("Alarm")
//            .setContentText("Your alarm is ringing!")
//            .setPriority(NotificationCompat.PRIORITY_HIGH) // High priority needed
//            .setCategory(NotificationCompat.CATEGORY_ALARM) // Important category
//            // Set the intent that will start when the notification is tapped (optional but good practice)
//            .setContentIntent(fullScreenPendingIntent)
//            // Set the intent that will be launched immediately as a full-screen activity.
//            .setFullScreenIntent(fullScreenPendingIntent, true) // Set 'true' for high priority
//            .setAutoCancel(true) // Dismiss notification when tapped
//
//        // Ensure you have POST_NOTIFICATIONS permission before calling notify() on API 33+
//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
//            == PackageManager.PERMISSION_GRANTED) {
//            // Unique notification ID
//            notificationManager.notify(456, builder.build())
//        } else {
//            // Handle missing permission - cannot show notification or full screen intent properly
//            Log.w("AlarmReceiver", "POST_NOTIFICATIONS permission missing")
//            // Fallback: Try starting activity directly (may fail on newer APIs from background)
//            // context.startActivity(fullScreenIntent) // Already tried above, consider alternative behavior
//        }

    }

    override fun onReceive(context: Context, intent: Intent) {
        // give receiver extra time if needed
        val pendingResult = goAsync()
        val alarmId = intent.getLongExtra(EXTRA_ALARM_ID, -1L)
        Log.d("AlarmReceiver", "Alarm triggered: ID")

        if (alarmId != -1L) {
            test(context, alarmId)
        }

        return

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


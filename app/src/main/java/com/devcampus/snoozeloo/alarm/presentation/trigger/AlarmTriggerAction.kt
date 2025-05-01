package com.devcampus.snoozeloo.alarm.presentation.trigger

import java.time.DayOfWeek

sealed interface AlarmTriggerAction {
    data object OnSnooze: AlarmTriggerAction
    data object OnDismiss: AlarmTriggerAction
}
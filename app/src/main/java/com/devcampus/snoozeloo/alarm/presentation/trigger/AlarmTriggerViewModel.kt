package com.devcampus.snoozeloo.alarm.presentation.trigger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devcampus.snoozeloo.alarm.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class AlarmTriggerViewModel(
    private val alarmRepository: AlarmRepository,
    val alarmId: Long
) : ViewModel()  {

    private val _state = MutableStateFlow(AlarmTriggerState())
    val state = _state
        .asStateFlow()
        .onStart {
            alarmRepository.getAlarm(alarmId)?.let { alarm ->
                _state.update {
                    it.copy(
                        alarm = alarm
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AlarmTriggerState()
        )

    fun onAction(action: AlarmTriggerAction) {
        when(action) {
            AlarmTriggerAction.OnDismiss -> {

            }
            AlarmTriggerAction.OnSnooze -> {

            }
        }
    }

}
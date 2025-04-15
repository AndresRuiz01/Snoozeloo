package com.devcampus.snoozeloo.alarm.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devcampus.snoozeloo.alarm.di.alarmModule
import com.devcampus.snoozeloo.alarm.domain.repository.AlarmRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlarmListViewModel(
    private val alarmRepository: AlarmRepository,
) : ViewModel() {

    private val _state = MutableStateFlow(AlarmListState())
    val state = _state
        .asStateFlow()
        .onStart {
            viewModelScope.launch {
                alarmRepository.getAlarmsFlow().collect { alarms ->
                    _state.update {
                        it.copy(
                            alarms = alarms
                        )
                    }
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = AlarmListState()
        )

    fun onAction(action: AlarmListAction) {
        when(action) {
            is AlarmListAction.ToggleAlarm -> {
                viewModelScope.launch {
                    _state.value.alarms.find { action.id == it.alarmId }?.let { alarm ->
                        alarmRepository.upsertAlarm(
                            alarm.copy(
                                enabled = !alarm.enabled
                            )
                        )
                    }
                }
            }
        }
    }

}

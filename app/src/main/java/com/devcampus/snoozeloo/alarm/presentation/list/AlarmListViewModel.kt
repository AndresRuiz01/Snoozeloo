package com.devcampus.snoozeloo.alarm.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devcampus.snoozeloo.alarm.domain.repository.AlarmRepository
import com.devcampus.snoozeloo.alarm.domain.use_case.UpsertAlarmUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AlarmListViewModel(
    private val alarmRepository: AlarmRepository,
    private val upsertAlarmUseCase: UpsertAlarmUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(AlarmListState())
    val state = _state
        .asStateFlow()
        .onStart {
            viewModelScope.launch {
                alarmRepository.getAlarmsFlow().collect { alarms ->
                    _state.update {
                        it.copy(
                            alarms = alarms.sortedBy { alarm -> alarm.time }
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
                        upsertAlarmUseCase(
                            alarm.copy(
                                enabled = !alarm.enabled
                            )
                        )
                    }
                }
            }

            is AlarmListAction.ToggleDay -> {
                val alarm = _state.value.alarms.find { action.id == it.alarmId }
                alarm?.let {
                    if(it.days.contains(action.day)) {
                        // remove day from alarm
                        viewModelScope.launch {
                            upsertAlarmUseCase(
                                it.copy(
                                    days = it.days.filter { it != action.day }
                                )
                            )
                        }
                    } else {
                        // add day to alarm
                        viewModelScope.launch {
                            upsertAlarmUseCase(
                                it.copy(
                                    days = it.days + action.day
                                )
                            )
                        }
                    }
                }
            }
        }
    }

}

package com.devcampus.snoozeloo.alarm.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devcampus.snoozeloo.alarm.domain.models.Alarm
import com.devcampus.snoozeloo.alarm.domain.repository.AlarmRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditAlarmViewModel(
    private val alarmRepository: AlarmRepository,
    val alarmId: Long?,
) : ViewModel() {

    private val eventChannel = Channel<EditAlarmEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(EditAlarmState())
    val state = _state
        .asStateFlow()
        .onStart {
            viewModelScope.launch {
                val id = alarmId ?: 0
                val alarm = alarmRepository.getAlarm(id) ?: Alarm.defaultAlarm
                _state.update {
                    it.copy(
                        originalAlarm = alarm,
                        alarm = alarm
                    )
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = EditAlarmState()
        )

    fun onAction(action: EditAlarmAction) {
        when(action) {
            is EditAlarmAction.ToggleVibrate -> {
                _state.update {
                    it.copy(
                        alarm = it.alarm?.copy(
                            enabled = action.enabled
                        )
                    )
                }
            }

            is EditAlarmAction.DayClicked -> {
                if (action.day in (_state.value.alarm?.days ?: listOf())) {
                    _state.update { previous ->
                        previous.copy(
                            alarm = previous.alarm?.copy(
                                days = previous.alarm.days.filter { it != action.day },
                            )
                        )
                    }
                } else {
                    _state.update { previous ->
                        previous.copy(
                            alarm = previous.alarm?.copy(
                                days = previous.alarm.days + action.day,
                            )
                        )
                    }
                }
            }

            is EditAlarmAction.OnVolumeChanged -> {
                _state.update { previous ->
                    previous.copy(
                        alarm = previous.alarm?.copy(
                            volume = action.volume
                        )
                    )
                }
            }

            EditAlarmAction.OnSave -> {
                viewModelScope.launch {
                    _state.value.alarm?.let {
                        val test = alarmRepository.upsertAlarm(it)
                        val tester = test == 1L
                        emitEvent(EditAlarmEvent.AlarmSaved)
                    }
                }
            }
        }
    }

    private fun emitEvent(event: EditAlarmEvent) {
        viewModelScope.launch { eventChannel.send(event) }
    }

}
package com.devcampus.snoozeloo.alarm.presentation.edit

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TimePickerState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devcampus.snoozeloo.alarm.domain.models.Alarm
import com.devcampus.snoozeloo.alarm.domain.repository.AlarmRepository
import com.devcampus.snoozeloo.alarm.domain.use_case.UpsertAlarmUseCase
import com.devcampus.snoozeloo.ringtone.domain.RingtoneRepository
import com.devcampus.snoozeloo.ringtone.domain.silentRingtone
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
class EditAlarmViewModel(
    private val alarmRepository: AlarmRepository,
    private val ringtoneRepository: RingtoneRepository,
    private val upsertAlarmUseCase: UpsertAlarmUseCase,
    val alarmId: Long?,
) : ViewModel() {

    private val eventChannel = Channel<EditAlarmEvent>()
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(EditAlarmState())
    val state = _state
        .asStateFlow()
        .onStart {
            viewModelScope.launch {
                val id = alarmId ?: 0L
                if (id == 0L) {
                    // new alarm, lets set it up accordingly
                    val alarm = Alarm.defaultAlarm.copy(
                        ringtone = ringtoneRepository.getDefaultRingtone() ?: silentRingtone
                    )
                    _state.update {
                        it.copy(
                            alarm = alarm,
                        )
                    }
                } else {
                    // one that exists, lets retrieve it from storage
                    val alarm = alarmRepository.getAlarm(id)
                    alarm?.let {
                        _state.update {
                            it.copy(
                                alarm = alarm,
                                timePickerState = TimePickerState(
                                    alarm.time.hour,
                                    alarm.time.minute,
                                    false
                                )
                            )
                        }
                    }
                    // update hour, minute, and AM/PM state
                }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = EditAlarmState()
        )

    fun onAction(action: EditAlarmAction) {
        when (action) {
            is EditAlarmAction.ToggleVibrate -> {
                _state.update {
                    it.copy(
                        alarm = it.alarm?.copy(
                            vibrate = action.enabled
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
                        upsertAlarmUseCase(it)
                        emitEvent(EditAlarmEvent.AlarmSaved)
                    }
                }
            }

            is EditAlarmAction.OnSaveTitle -> {
                _state.update {
                    it.copy(
                        alarm = it.alarm?.copy(
                            title = action.title.trim()
                        ),
                        showEditTitle = false
                    )
                }
            }

            EditAlarmAction.HideEditTitle -> {
                _state.update {
                    it.copy(
                        showEditTitle = false
                    )
                }
            }

            EditAlarmAction.ShowEditTitle -> {
                _state.update {
                    it.copy(
                        showEditTitle = true
                    )
                }
            }

            is EditAlarmAction.OnRingtoneChanged -> {
                _state.update {
                    it.copy(
                        alarm = it.alarm?.copy(
                            ringtone = action.ringtone
                        ),
                    )
                }
            }

            is EditAlarmAction.OnTimeChanged -> {
                _state.update {
                    it.copy(
                        alarm = it.alarm?.copy(
                            time = action.time
                        ),
                        showTimePicker = false,
                        timePickerState = TimePickerState(
                            it.timePickerState.hour,
                            it.timePickerState.minute,
                            false
                        ) // resetting time picker state so it will focus on hour again
                    )
                }
            }

            EditAlarmAction.HideTimePicker -> {
                // not saved so we need to reset the picker in case the
                // time was changed there but canceled
                _state.update {
                    it.copy(
                        showTimePicker = false,
                        timePickerState = TimePickerState(
                            initialHour = _state.value.alarm?.time?.hour ?: 0,
                            initialMinute = _state.value.alarm?.time?.minute ?: 0,
                            is24Hour = false
                        )
                    )
                }
            }

            EditAlarmAction.ShowTimePicker -> {
                _state.update {
                    it.copy(
                        showTimePicker = true,
                    )
                }
            }
        }
    }

    private fun emitEvent(event: EditAlarmEvent) {
        viewModelScope.launch { eventChannel.send(event) }
    }
}
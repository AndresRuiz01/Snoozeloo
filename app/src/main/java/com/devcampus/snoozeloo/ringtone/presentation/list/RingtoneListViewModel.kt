package com.devcampus.snoozeloo.ringtone.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devcampus.snoozeloo.ringtone.domain.Ringtone
import com.devcampus.snoozeloo.ringtone.domain.RingtonePlayer
import com.devcampus.snoozeloo.ringtone.domain.RingtoneRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface RingtoneListEvent {
    data object OnNavigateBack: RingtoneListEvent
}

class RingtoneListViewModel(
    private val ringtoneRepository: RingtoneRepository,
    private val ringtonePlayer: RingtonePlayer,
    val initialRingtone: Ringtone,
) : ViewModel() {

    private val eventChannel = Channel<RingtoneListEvent>(Channel.BUFFERED)
    val events = eventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(RingtoneListState())
    val state = _state
        .asStateFlow()
        .onStart {
            val options = ringtoneRepository.getRingtones()
            _state.update {
                it.copy(
                    ringtoneOptions = options,
                    currentRingtone = options.find { option -> initialRingtone.uri == option.uri }
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = RingtoneListState()
        )

    fun onAction(action: RingtoneListAction) {
        when(action) {
            is RingtoneListAction.RingtoneClicked -> {
                ringtonePlayer.play(action.ringtone)
                _state.update {
                    it.copy(
                        currentRingtone = action.ringtone
                    )
                }
            }

            RingtoneListAction.OnNavigateBack -> {
                ringtonePlayer.stop()
                emitEvent(RingtoneListEvent.OnNavigateBack)
            }

            RingtoneListAction.OnBackground -> {
                ringtonePlayer.stop()
            }
        }
    }

    fun emitEvent(event: RingtoneListEvent) {
        viewModelScope.launch { eventChannel.send(event) }
    }

}
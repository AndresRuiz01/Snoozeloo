package com.devcampus.snoozeloo.ringtone.presentation.list

import com.devcampus.snoozeloo.ringtone.domain.Ringtone

sealed interface RingtoneListAction {
    data class RingtoneClicked(val ringtone: Ringtone): RingtoneListAction
    data object OnBackground: RingtoneListAction
    data object OnNavigateBack: RingtoneListAction
}
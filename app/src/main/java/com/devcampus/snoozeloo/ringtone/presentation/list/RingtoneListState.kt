package com.devcampus.snoozeloo.ringtone.presentation.list

import com.devcampus.snoozeloo.ringtone.domain.Ringtone

data class RingtoneListState(
    val ringtoneOptions: List<Ringtone> = listOf(),
    val currentRingtone: Ringtone? = null,
)
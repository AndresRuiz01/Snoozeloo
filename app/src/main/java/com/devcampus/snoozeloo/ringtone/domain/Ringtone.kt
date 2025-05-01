package com.devcampus.snoozeloo.ringtone.domain

import kotlinx.serialization.Serializable

@Serializable
data class Ringtone(
    val name: String,
    val uri: String,
)

val silentRingtone = Ringtone(name = "Silent", uri = "")
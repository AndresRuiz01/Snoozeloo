package com.devcampus.snoozeloo.ringtone.domain

interface RingtonePlayer {
    fun play(ringtone: Ringtone)
    fun stop()
    fun cleanup()
}
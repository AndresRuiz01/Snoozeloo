package com.devcampus.snoozeloo.ringtone.domain

interface RingtoneRepository {
    fun getRingtones(): List<Ringtone>
    fun getRingtone(uri: String): Ringtone?
    fun getDefaultRingtone(): Ringtone?
}
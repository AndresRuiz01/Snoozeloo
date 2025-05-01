package com.devcampus.snoozeloo.ringtone.data

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import com.devcampus.snoozeloo.ringtone.domain.Ringtone
import com.devcampus.snoozeloo.ringtone.domain.RingtoneRepository
import com.devcampus.snoozeloo.ringtone.domain.silentRingtone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class AndroidRingtoneRepository(
    private val context: Context,
) : RingtoneRepository {

    override fun getRingtones(): List<Ringtone> {
        val ringtones = mutableSetOf(silentRingtone)
        val ringtoneManager = RingtoneManager(context)
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM)
        val cursor = ringtoneManager.cursor
        while(!cursor.isAfterLast) {
            val title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX)
            val uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX) + "/" + cursor.getString(
                RingtoneManager.ID_COLUMN_INDEX)
            ringtones.add(
                Ringtone(
                    name = title,
                    uri = uri
                )
            )
            cursor.moveToNext()
        }
        return ringtones.toList()
    }

    private fun getRingtoneFromUri(uri: Uri): Ringtone? {
        try {
            val ringtone = RingtoneManager.getRingtone(context, uri)
            val title = ringtone.getTitle(context)
            val baseUri: String = Uri.Builder()
                .scheme(uri.scheme)
                .authority(uri.authority)
                .path(uri.path)
                .build()
                .toString()
            return Ringtone(
                name = title,
                uri = baseUri
            )
        } catch(ex: Exception) {
            // default ringtone doesn't exist...
            Log.e("AndroidRingtoneRepository", "No ringtone found")
            return null
        }
    }

    override fun getRingtone(uri: String): Ringtone? {
        try {
            if (uri.isEmpty()) return silentRingtone
            return getRingtoneFromUri(Uri.parse(uri))
        } catch(ex: Exception) {
            // default ringtone doesn't exist...
            Log.e("AndroidRingtoneRepository", "No ringtone found with uri: $uri")
            return null
        }
    }

    override fun getDefaultRingtone(): Ringtone? {
        try {
            val defaultUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM)
            return getRingtoneFromUri(defaultUri)
        } catch(ex: Exception) {
            // default ringtone doesn't exist...
            Log.e("AndroidRingtoneRepository", "Default ringtone not found")
            return null
        }
    }
}



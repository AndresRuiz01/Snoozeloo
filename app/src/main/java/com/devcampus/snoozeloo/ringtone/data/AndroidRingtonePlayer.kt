package com.devcampus.snoozeloo.ringtone.data

import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import com.devcampus.snoozeloo.ringtone.domain.Ringtone
import com.devcampus.snoozeloo.ringtone.domain.RingtonePlayer
import com.devcampus.snoozeloo.ringtone.domain.silentRingtone
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AndroidRingtonePlayer(
    private val context: Context,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
) : RingtonePlayer {

    private var currentRingtone: android.media.Ringtone? = null
    private var stopJob: Job? = null // Job to track the delayed stop task

    override fun play(ringtone: Ringtone) {
        // 1. Stop any existing playback and cancel the pending stop job
        stopInternal()

        if (ringtone == silentRingtone) return // nothing to play

        try {
            // 2. Get the new ringtone
            val androidRingtone = RingtoneManager.getRingtone(context, Uri.parse(ringtone.uri))

            if (androidRingtone == null) {
                Log.e("AndroidRingtonePlayer", "Failed to get ringtone for URI: $ringtone.uri")
                // Optionally handle the error further (e.g., show a message)
                return
            }

            // 3. Store reference and start playing
            currentRingtone = androidRingtone
            currentRingtone?.play()
            Log.d("AndroidRingtonePlayer", "Playing ringtone: $ringtone.uri")

            // 4. Schedule stop after 10 seconds
            stopJob = coroutineScope.launch {
                delay(10_000L) // 10 seconds delay
                Log.d("AndroidRingtonePlayer", "Auto-stopping ringtone after 10 seconds: $ringtone.uri")
                // Call the public stop() method to ensure consistent cleanup
                stop()
            }

        } catch (e: SecurityException) {
            Log.e("AndroidRingtonePlayer", "Permission denied for URI: $ringtone.uri", e)
            // Handle permission errors
            stopInternal() // Clean up any partial state
        } catch (e: Exception) {
            Log.e("AndroidRingtonePlayer", "Error playing ringtone for URI: $ringtone.uri", e)
            // Handle other potential errors
            stopInternal() // Clean up any partial state
        }
    }

    override fun stop() {
        Log.d("AndroidRingtonePlayer", "Stop called explicitly.")
        stopInternal()
    }

    /**
     * Internal function to handle stopping playback, cancelling timers,
     * and clearing references. Called by both play() (before starting a new one)
     * and stop().
     */
    private fun stopInternal() {
        // Cancel the scheduled stop task if it's active
        stopJob?.cancel()
        stopJob = null

        // Stop the ringtone playback if it's playing
        try {
            if (currentRingtone?.isPlaying == true) {
                Log.d("AndroidRingtonePlayer", "Stopping current ringtone.")
                currentRingtone?.stop()
            }
        } catch (e: Exception) {
            // Catch potential exceptions during stop, though less common
            Log.e("AndroidRingtonePlayer", "Error stopping ringtone", e)
        } finally {
            // Clear the reference regardless of success/failure
            currentRingtone = null
        }
    }

    // Optional: Add a cleanup method if the scope needs explicit cancellation
    // e.g., if the scope is tied to a ViewModel, it's often cancelled automatically.
     override fun cleanup() {
         stopInternal()
         coroutineScope.cancel() // Cancel the scope if managed internally
     }
}
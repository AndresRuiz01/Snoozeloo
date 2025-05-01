package com.devcampus.snoozeloo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.devcampus.snoozeloo.alarm.presentation.edit.EditAlarmScreenRoot
import com.devcampus.snoozeloo.alarm.presentation.list.AlarmListScreenRoot
import com.devcampus.snoozeloo.ringtone.domain.Ringtone
import com.devcampus.snoozeloo.ringtone.presentation.list.RingtoneListScreenRoot
import kotlinx.serialization.Serializable

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Home
    ) {
        composable<Home> {
            AlarmListScreenRoot(
                navigateToEditAlarm = { id ->
                    navController.navigate(
                        Edit(id)
                    )
                }
            )
        }

        composable<Edit> { entry ->
            val alarmId = entry.toRoute<Edit>()
            val name = entry.savedStateHandle.get<String>("ringtone_name")
            val uri = entry.savedStateHandle.get<String>("ringtone_uri")
            val ringtoneFromList = if (name != null && uri != null) {
                Ringtone(
                    name = name,
                    uri = uri
                )
            } else null
            EditAlarmScreenRoot(
                alarmId = alarmId.id,
                ringtoneFromList = ringtoneFromList,
                navigateBack = {
                    navController.navigateUp()
                },
                navigateToRingtoneList = {
                    navController.navigate(RingtoneList(it.name, it.uri))
                }
            )
        }

        composable<RingtoneList> { entry ->
            val route = entry.toRoute<RingtoneList>()
            RingtoneListScreenRoot(
                initialRingtone = Ringtone(route.ringtoneName, route.ringtoneUri),
                navigateBack = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("ringtone_name", it.name)
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("ringtone_uri", it.uri)
                    navController.navigateUp()
                }
            )
        }
    }
}

@Serializable
object Home

@Serializable
data class Edit(
    val id: Long?
)

@Serializable
data class RingtoneList(
    val ringtoneName: String,
    val ringtoneUri: String
)
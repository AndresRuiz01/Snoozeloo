package com.devcampus.snoozeloo

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.devcampus.snoozeloo.alarm.presentation.edit.EditAlarmScreenRoot
import com.devcampus.snoozeloo.alarm.presentation.list.AlarmListScreenRoot
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

        composable<Edit> {
            val alarmId = it.toRoute<Edit>()
            EditAlarmScreenRoot(
                alarmId = alarmId.id,
                navigateBack = {
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
package com.devcampus.snoozeloo.alarm.presentation.trigger

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun AlarmTriggerScreenRoot(
    alarmId: Long,
    modifier: Modifier = Modifier,
    viewModel: AlarmTriggerViewModel = koinViewModel<AlarmTriggerViewModel>(parameters = { parametersOf(alarmId) })
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    AlarmTriggerScreen(
        state = state,
        onAction = viewModel::onAction
    )

}

@Composable
fun AlarmTriggerScreen(
    state: AlarmTriggerState,
    onAction: (AlarmTriggerAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.Blue))
}
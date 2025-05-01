package com.devcampus.snoozeloo.alarm.presentation.edit

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devcampus.snoozeloo.alarm.presentation.edit.components.DaysCell
import com.devcampus.snoozeloo.alarm.presentation.edit.components.EditAlarmScaffold
import com.devcampus.snoozeloo.alarm.presentation.edit.components.SliderCell
import com.devcampus.snoozeloo.alarm.presentation.edit.components.TextCell
import com.devcampus.snoozeloo.alarm.presentation.edit.components.TimeColon
import com.devcampus.snoozeloo.alarm.presentation.edit.components.TimePickerDialog
import com.devcampus.snoozeloo.alarm.presentation.edit.components.TitleInput
import com.devcampus.snoozeloo.alarm.presentation.edit.components.ToggleCell
import com.devcampus.snoozeloo.core.presentation.ObserveAsEvents
import com.devcampus.snoozeloo.ringtone.domain.Ringtone
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.LocalTime

sealed interface EditAlarmEvent {
    data object AlarmSaved : EditAlarmEvent
}

@Composable
fun EditAlarmScreenRoot(
    alarmId: Long?,
    ringtoneFromList: Ringtone?,
    navigateBack: () -> Unit,
    navigateToRingtoneList: (Ringtone) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditAlarmViewModel = koinViewModel<EditAlarmViewModel>(parameters = { parametersOf(alarmId) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(ringtoneFromList) {
        if (ringtoneFromList != null) {
            viewModel.onAction(EditAlarmAction.OnRingtoneChanged(ringtoneFromList))
        }
    }

    ObserveAsEvents(viewModel.events) { event: EditAlarmEvent ->
        when (event) {
            EditAlarmEvent.AlarmSaved -> navigateBack()
        }
    }

    EditAlarmScreen(
        navigateBack = navigateBack,
        navigateToRingtoneList = navigateToRingtoneList,
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAlarmScreen(
    navigateBack: () -> Unit,
    navigateToRingtoneList: (Ringtone) -> Unit,
    onAction: (EditAlarmAction) -> Unit,
    state: EditAlarmState,
    modifier: Modifier = Modifier
) {

    // In the event the edit title popup is visible, lets close that when a user uses the
    // system back navigation
    BackHandler(state.showEditTitle) {
        onAction(EditAlarmAction.HideEditTitle)
    }

    EditAlarmScaffold(
        navigateBack = {
            navigateBack()
        },
        onSave = {
            onAction(EditAlarmAction.OnSave)
        }
    ) { innerPadding ->

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {

            item {
                TimeCell(
                    time = state.alarm?.time ?: LocalTime.of(0,0),
                    onClick = {
                        onAction(EditAlarmAction.ShowTimePicker)
                    }
                )
            }

            item {
                TextCell(
                    title = "Alarm Name",
                    subtitle = state.alarm?.title ?: "",
                    onClick = {
                        onAction(EditAlarmAction.ShowEditTitle)
                    }
                )
            }

            item {
                DaysCell(
                    title = "Repeat",
                    days = state.alarm?.days ?: listOf(),
                    onDayClicked = {
                        onAction(EditAlarmAction.DayClicked(it))
                    }
                )
            }

            item {
                TextCell(
                    title = "Alarm Ringtone",
                    subtitle = state.alarm?.ringtone?.name ?: "Silent",
                    onClick = {
                        state.alarm?.ringtone?.let {
                            navigateToRingtoneList(it)
                        }
                    }
                )
            }

            item {
                SliderCell(
                    title = "Alarm Volume",
                    value = state.alarm?.volume?.toFloat() ?: 100f,
                    onValueChanged = { onAction(EditAlarmAction.OnVolumeChanged(it.toInt())) }
                )
            }

            item {
                ToggleCell(
                    title = "Vibrate",
                    checked = state.alarm?.vibrate ?: false,
                    onCheckedChanged = { onAction(EditAlarmAction.ToggleVibrate(it)) }
                )
            }
        }
    }

    if (state.showTimePicker) {
        TimePickerDialog(
            state = state.timePickerState,
            onDismiss = { onAction(EditAlarmAction.HideTimePicker) },
            onConfirm = {
                // Read the final hour and minute from the state
                val hour = state.timePickerState.hour
                val minute = state.timePickerState.minute
                onAction(EditAlarmAction.OnTimeChanged(LocalTime.of(hour, minute)))
            }
        )
    }

    AnimatedVisibility(
        visible = state.showEditTitle,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        TitleInput(
            initialTitle = state.alarm?.title,
            onHide = {
                onAction(EditAlarmAction.HideEditTitle)
            },
            onSaveTitle = {
                onAction(EditAlarmAction.OnSaveTitle(it))
            }
        )
    }
}

@Composable
private fun TimeCell(
    time: LocalTime,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // display logic for 12 hour clock
    val isAm = time.hour < 12
    var hour = time.hour
    if (!isAm && hour > 12) hour -= 12
    if (isAm && hour == 0) hour = 12
    val minute = time.minute
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp)
            .height(95.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF6F6F6))
                .clickable {
                    onClick()
                }
        ) {
            Text(
                text = hour.toString().padStart(2, '0'),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 44.sp,
                    color = Color(0xFF858585),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
            )
        }
        // colon between hour and minute
        TimeColon()
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(1f)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFF6F6F6))
                .clickable {
                    onClick()
                }
        ) {
            Text(
                text = minute.toString().padStart(2, '0'),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 44.sp,
                    color = Color(0xFF858585),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .wrapContentSize()
            )
        }
        Column(
            modifier = Modifier
                .width(50.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(8.dp))
                .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .then(
                        if (isAm) {
                            Modifier.background(Color(0xFF4664FF))
                        } else {
                            Modifier
                        }
                    )
                    .clickable {
                        onClick()
                    }

            ) {
                Text(
                    text = "AM",
                    color = if (isAm) Color.White else Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            }
            Spacer(modifier = Modifier
                .height(1.dp)
                .fillMaxWidth()
                .background(Color.Black))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .then(
                        if (!isAm) {
                            Modifier.background(Color(0xFF4664FF))
                        } else {
                            Modifier
                        }
                    )
                    .clickable {
                        onClick()
                    }
            ) {
                Text(
                    text = "PM",
                    color = if (!isAm) Color.White else Color.Black,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxSize()
                        .wrapContentSize()
                )
            }
        }
    }
}
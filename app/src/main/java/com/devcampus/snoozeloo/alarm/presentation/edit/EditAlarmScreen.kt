package com.devcampus.snoozeloo.alarm.presentation.edit

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.devcampus.snoozeloo.alarm.presentation.components.DayCell
import com.devcampus.snoozeloo.ui.theme.SnoozelooBackground
import com.devcampus.snoozeloo.ui.theme.montserrat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import java.time.DayOfWeek

sealed interface EditAlarmEvent {
    data object AlarmSaved : EditAlarmEvent
}

@Composable
fun EditAlarmScreenRoot(
    alarmId: Long?,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditAlarmViewModel = koinViewModel<EditAlarmViewModel>(parameters = { parametersOf(alarmId) })
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            EditAlarmEvent.AlarmSaved -> navigateBack()
        }
    }

    EditAlarmScreen(
        navigateBack = navigateBack,
        state = state,
        onAction = viewModel::onAction,
        modifier = modifier
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAlarmScreen(
    navigateBack: () -> Unit,
    onAction: (EditAlarmAction) -> Unit,
    state: EditAlarmState,
    modifier: Modifier = Modifier
) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFFE6E6E6))
                            .clickable {
                                navigateBack()
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxSize()
                                .wrapContentSize(),
                            tint = SnoozelooBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent
                ),
                windowInsets = TopAppBarDefaults.windowInsets
                    .only(WindowInsetsSides.Top)
                    .union(WindowInsets(right = 16.dp)),
                actions = {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(100f))
                            .background(
                                if(state.isSaveEnabled) {
                                    MaterialTheme.colorScheme.primary
                                } else {
                                    Color(0xFFE6E6E6)
                                }
                            )
                            .clickable(state.isSaveEnabled) {
                                onAction(EditAlarmAction.OnSave)
                            }
                            .padding(vertical = 6.dp, horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Save",
                            style = TextStyle(
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = montserrat,
                                fontSize = 16.sp
                            ),
                            color = Color.White
                        )
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            TextCell(
                title = "Alarm Name",
                subtitle = "Work",
                onClick = { }
            )
            DaysCell(
                title = "Repeat",
                days = state.alarm?.days ?: listOf(),
                onDayClicked = {
                    onAction(EditAlarmAction.DayClicked(it))
                }
            )
            TextCell(
                title = "Alarm Ringtone",
                subtitle = "Default",
                onClick = { }
            )
            SliderCell(
                title = "Alarm Volume",
                value = state.alarm?.volume?.toFloat() ?: 100f,
                onValueChanged = { onAction(EditAlarmAction.OnVolumeChanged(it.toInt())) }
            )
            ToggleCell(
                title = "Vibrate",
                checked = state.alarm?.enabled ?: false,
                onCheckedChanged = { onAction(EditAlarmAction.ToggleVibrate(it)) }
            )
        }
    }
}

@Composable
private fun TextCell(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .clickable {
                onClick()
            }
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color(0xFF0D0F19),
                fontSize = 16.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.SemiBold
            )
        )
        Text(
            text = subtitle,
            style = TextStyle(
                color = Color(0xFF858585),
                fontSize = 14.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.Medium
            )
        )
    }
}

@Composable
private fun ToggleCell(
    title: String,
    checked: Boolean,
    onCheckedChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color(0xFF0D0F19),
                fontSize = 16.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.SemiBold
            )
        )
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChanged,
            colors = SwitchDefaults.colors().copy(
                uncheckedTrackColor = Color(0xFFBCC6FF),
                uncheckedThumbColor = Color.White,
                uncheckedBorderColor = Color.White,
            ),
        )
    }
}

@Composable
private fun DaysCell(
    title: String,
    days: List<DayOfWeek>,
    onDayClicked: (DayOfWeek) -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color(0xFF0D0F19),
                fontSize = 16.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.SemiBold
            )
        )
        DayCell(
            days = days,
            onDayClicked = onDayClicked
        )
    }
}

@Composable
private fun SliderCell(
    title: String,
    value: Float,
    onValueChanged: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = title,
            style = TextStyle(
                color = Color(0xFF0D0F19),
                fontSize = 16.sp,
                fontFamily = montserrat,
                fontWeight = FontWeight.SemiBold
            )
        )

        Slider(
            value = value,
            onValueChange = onValueChanged,
            valueRange = 0f..100f, // Range from 0 to 100
            steps = 99,
            colors = SliderDefaults.colors().copy(
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
                inactiveTrackColor = Color(0xFFECEFFF)
            )
        )
    }
}

@Composable
fun <T> ObserveAsEvents(
    flow: Flow<T>,
    key1: Any? = null,
    key2: Any? = null,
    onEvent: (T) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(flow, lifecycleOwner.lifecycle, key1, key2) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            withContext(Dispatchers.Main.immediate) {
                flow.collect(onEvent)
            }
        }
    }
}
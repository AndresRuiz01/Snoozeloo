package com.devcampus.snoozeloo.alarm.presentation.list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devcampus.snoozeloo.alarm.presentation.list.components.AlarmCell
import com.devcampus.snoozeloo.alarm.presentation.list.components.NoAlarmCell
import com.devcampus.snoozeloo.ui.theme.montserrat
import org.koin.androidx.compose.koinViewModel


@Composable
fun AlarmListScreenRoot(
    navigateToEditAlarm: (Long?) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AlarmListViewModel = koinViewModel<AlarmListViewModel>()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    AlarmListScreen(
        navigateToEditAlarm = navigateToEditAlarm,
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlarmListScreen(
    navigateToEditAlarm: (Long?) -> Unit,
    state: AlarmListState,
    onAction: (AlarmListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Your Alarms",
                        style = TextStyle(
                            fontFamily = montserrat,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Medium
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors().copy(
                    containerColor = Color.Transparent
                ),
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateToEditAlarm(null)
                },
                shape = CircleShape,
                modifier = Modifier.size(70.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (state.alarms.isEmpty()) {
                NoAlarmCell(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = innerPadding.calculateTopPadding())
                        .background(Color(0xFFF6F6F6))
                        .padding(horizontal = 16.dp)
                ) {
                    items(
                        items = state.alarms,
                        key = { it.alarmId }
                    ) { alarm ->
                        AlarmCell(
                            title = alarm.title,
                            time = alarm.time,
                            enabled = alarm.enabled,
                            days = alarm.days,
                            onEnabledChanged = {
                                onAction(AlarmListAction.ToggleAlarm(alarm.alarmId))
                            },
                            onAlarmClicked = {
                                navigateToEditAlarm(alarm.alarmId)
                            }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(120.dp)) }
                }
            }
        }
    }
}
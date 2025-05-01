package com.devcampus.snoozeloo.ringtone.presentation.list

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devcampus.snoozeloo.R
import com.devcampus.snoozeloo.core.presentation.ObserveAsEvents
import com.devcampus.snoozeloo.ringtone.domain.Ringtone
import com.devcampus.snoozeloo.ringtone.presentation.list.components.RingtoneOption
import com.devcampus.snoozeloo.ui.theme.SnoozelooBackground
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RingtoneListScreenRoot(
    initialRingtone: Ringtone,
    navigateBack: (Ringtone) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RingtoneListViewModel = koinViewModel<RingtoneListViewModel>(parameters = { parametersOf(initialRingtone) })
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is RingtoneListEvent.OnNavigateBack  -> {
                state.currentRingtone?.let {
                    navigateBack(it)
                }
            }
        }
    }

    RingtoneListScreen(
        state = state,
        onAction = viewModel::onAction,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RingtoneListScreen(
    state: RingtoneListState,
    onAction: (RingtoneListAction) -> Unit,
    modifier: Modifier = Modifier
) {
    // handle sending the data back
    BackHandler {
        onAction(RingtoneListAction.OnNavigateBack)
    }

    OnBackgroundListener(
        onBackground = {
            onAction(RingtoneListAction.OnBackground)
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(0xFF4664FF))
                            .clickable {
                                onAction(RingtoneListAction.OnNavigateBack)
                            }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
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
            )
        }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {
            items(state.ringtoneOptions) { ringtone ->
                RingtoneOption(
                    title = ringtone.name,
                    isSelected = state.currentRingtone == ringtone,
                    icon = if (ringtone.uri.isNotEmpty()) R.drawable.sound else R.drawable.silent,
                    onClick = {
                        onAction(RingtoneListAction.RingtoneClicked(ringtone))
                    }
                )
            }
        }
    }
}

@Composable
fun OnBackgroundListener(
    onBackground: () -> Unit
) {
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    // Use DisposableEffect to add/remove the observer safely
    DisposableEffect(lifecycleOwner) {
        // Create a LifecycleEventObserver
        val observer = LifecycleEventObserver { _, event ->
            // Check the lifecycle event
            when (event) {
                Lifecycle.Event.ON_STOP -> {
                    onBackground()
                }
                else -> {}
            }
        }

        // Add the observer to the lifecycle
        lifecycleOwner.lifecycle.addObserver(observer)

        // Specify the cleanup logic for when the composable leaves composition
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
}
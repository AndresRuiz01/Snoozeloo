package com.devcampus.snoozeloo.alarm.presentation.edit.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    title: String = "Select Time",
    state: TimePickerState,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(bottom = 16.dp))
                TimePicker(
                    state = state,
                    colors = TimePickerDefaults.colors().copy(
                        containerColor = Color.White,
                        timeSelectorSelectedContainerColor = Color(0xFFF6F6F6),
                        timeSelectorSelectedContentColor = Color(0xFF4664FF),
                        timeSelectorUnselectedContainerColor = Color(0xFFF6F6F6),
                        timeSelectorUnselectedContentColor = Color(0xFF858585),
                        periodSelectorBorderColor = Color.Black,
                        periodSelectorSelectedContainerColor = Color(0xFF4664FF),
                        periodSelectorSelectedContentColor = Color.White,
                        periodSelectorUnselectedContainerColor = Color.White,
                        periodSelectorUnselectedContentColor = Color.Black,
                        selectorColor = Color(0xFF4664FF),
                        clockDialColor = Color(0xFFF6F6F6)
                    )
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        },
        containerColor = Color.White,
    )
}
package com.devcampus.snoozeloo.alarm.presentation.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devcampus.snoozeloo.ui.theme.montserrat

@Composable
fun TitleInput(
    initialTitle: String?,
    onSaveTitle: (String) -> Unit,
    onHide: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xDDE6E6E6))
            .clickable(
                indication = null,
                interactionSource = null
            ) {
                onHide()
            }
            .padding(horizontal = 16.dp)
            .imePadding()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    indication = null,
                    interactionSource = null
                ) { }
                .clip(
                    RoundedCornerShape(10.dp)
                )
                .background(Color.White)
                .padding(16.dp)
                .align(Alignment.Center)
        ) {
            Text(
                text = "Alarm Name",
                style = TextStyle(
                    fontFamily = montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color(0xFF0D0F19)
                )
            )

            val focusRequester = remember { FocusRequester() }
            var tempTitle by rememberSaveable(stateSaver = TextFieldValue.Saver) {
                mutableStateOf(TextFieldValue(initialTitle ?: "", TextRange((initialTitle ?: "").length)))
            }
            OutlinedTextField(
                value = tempTitle,
                onValueChange = {
                    tempTitle = it
                },

                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }

            Button(
                onClick = {
                    onSaveTitle(tempTitle.text)
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(
                    text = "Save",
                    style = TextStyle(
                        fontFamily = montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                )
            }

        }
    }
}
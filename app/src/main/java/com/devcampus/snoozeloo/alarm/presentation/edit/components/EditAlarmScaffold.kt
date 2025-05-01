package com.devcampus.snoozeloo.alarm.presentation.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devcampus.snoozeloo.ui.theme.SnoozelooBackground
import com.devcampus.snoozeloo.ui.theme.montserrat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAlarmScaffold(
    navigateBack: () -> Unit,
    onSave: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable (PaddingValues) -> Unit
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
                            .background(MaterialTheme.colorScheme.primary)
                            .clickable {
                                onSave()
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
        content(innerPadding)
    }
}
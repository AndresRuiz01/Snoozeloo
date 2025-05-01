package com.devcampus.snoozeloo.ringtone.presentation.list.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devcampus.snoozeloo.R
import com.devcampus.snoozeloo.ui.theme.montserrat

@Composable
fun RingtoneOption(
    title: String,
    @DrawableRes icon: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
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
            .padding(vertical = 10.dp, horizontal = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(30.dp)
                    .clip(RoundedCornerShape(100f))
                    .background(Color(0xFFF6F6F6))
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().wrapContentSize()
                )
            }
            Text(
                text = title,
                style = TextStyle(fontFamily = montserrat, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
            )
        }
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(RoundedCornerShape(100f))
                    .background(Color(0xFF4664FF))
            ) {
                Icon(
                    painter = painterResource(R.drawable.check),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(14.dp).align(Alignment.Center)
                )
            }
        }
    }
}
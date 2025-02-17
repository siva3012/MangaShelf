package com.example.managshelf.ui.uiScreens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun YearTab(
    year: String,
    isSelected: Boolean,
    onClick: () -> Unit,
) {

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF007AFF) else Color(0xFF222222), // Blue when selected, dark gray otherwise
        animationSpec = tween(durationMillis = 300),
        label = "backgroundColor"
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color.White else Color(0xFFBBBBBB), // White when selected, light gray otherwise
        animationSpec = tween(durationMillis = 300),
        label = "textColor"
    )

    Box(
        modifier = Modifier
            .size(100.dp,50.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 8.dp)

        ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = year,
            color = textColor,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun YearShimmer(){
    Box(
        modifier = Modifier
            .size(100.dp,50.dp)
            .background(color = Color.Black)
            .clip(RoundedCornerShape(16.dp))
            .shimmerAnimationEffect()
            .padding(horizontal = 8.dp, vertical = 8.dp)
        ,
    )
}





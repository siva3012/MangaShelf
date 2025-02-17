package com.example.managshelf.ui.uiScreens

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntSize

fun Modifier.shimmerAnimationEffect(): Modifier = composed {
    val size = remember { mutableStateOf(IntSize.Zero) }
    val transition = rememberInfiniteTransition(label = "")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.value.width.toFloat(),
        targetValue = 2 * size.value.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        ), label = ""
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                Color(0xFFE4E2E2),
                Color(0xFFBEBDBD),
                Color(0xFFE4E2E2),
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.value.width.toFloat(), size.value.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size.value = it.size
        }
}
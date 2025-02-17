package com.example.managshelf.ui.uiScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MangaCardshimmer() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(2.dp, Color.White, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .shimmerAnimationEffect()
            )
    }
}


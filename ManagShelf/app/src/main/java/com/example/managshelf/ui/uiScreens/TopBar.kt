package com.example.managshelf.ui.uiScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import coil.compose.rememberAsyncImagePainter
import com.example.managshelf.R
import com.example.managshelf.ui.common.SortOption
import com.example.managshelf.ui.uiScreens.viewmodel.MangaViewModel


@Composable
fun TopBar(viewModel: MangaViewModel,onFilterClick:()->Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(Color(0xFF1E1E1E), Color(0xFF3A3A3A))))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "MangaShelf",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        IconButton(onClick = onFilterClick) {
            Icon(
                painter = painterResource(id = R.drawable.settings_16186609),
                contentDescription = "Settings",
                tint = Color.White,
                modifier = Modifier
                    .size(24.dp)
                    .onGloballyPositioned { coordinates ->
                        val offsetInRoot =
                            coordinates.positionInRoot()

                        viewModel.offset.value =
                            IntOffset(offsetInRoot.x.toInt(), offsetInRoot.y.toInt())
                    }
            )
        }
    }
}

@Composable
fun SideBar(viewModel: MangaViewModel, onDismiss: () -> Unit, onSortSelected: (SortOption) -> Unit) {
    val density = LocalDensity.current

    Popup(
        offset = IntOffset(
            viewModel.offset.value.x - with(density) { 220.dp.toPx().toInt() },
            viewModel.offset.value.y + 35
        ),
    ) {
        Column(
            modifier = Modifier
                .wrapContentHeight()
                .width(250.dp)
                .background(
                    Brush.horizontalGradient(listOf(Color(0xFF1E1E1E), Color(0xFF3A3A3A))),
                    RoundedCornerShape(12.dp)
                )
                .padding(16.dp)
        ) {
            Text("Sort by", fontSize = 20.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            SortOption.entries.forEach { option ->
                Text(
                    text = option.displayName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSortSelected(option); onDismiss() }
                        .padding(vertical = 10.dp, horizontal = 12.dp),
                    fontSize = 18.sp,
                    color = Color(0xFFB0B0B0)
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

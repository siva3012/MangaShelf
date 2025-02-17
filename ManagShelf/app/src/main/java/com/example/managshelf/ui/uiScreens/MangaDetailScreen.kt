package com.example.managshelf.ui.uiScreens

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.managshelf.R
import com.example.managshelf.ui.common.Utils
import com.example.managshelf.ui.uiScreens.viewmodel.MangaViewModel
import kotlinx.coroutines.delay

@Composable
fun MangaDetailScreen(navController: NavController, viewModel: MangaViewModel) {
    val manga by viewModel.selectedManga.collectAsState()

    var retryTrigger by remember { mutableStateOf(0) }

    val painter = rememberAsyncImagePainter(
        model = "${manga?.imageUrl}?reload=$retryTrigger"
    )
    val context = LocalContext.current
    val isLoading = painter.state is AsyncImagePainter.State.Loading
    val isError = painter.state is AsyncImagePainter.State.Error

    LaunchedEffect(isError) {
        if (isError) {
            delay(5000)
            retryTrigger++
        }
    }

    BackHandler {
        manga?.let { viewModel.updateMangaDetails(it) }
        navController.popBackStack()
    }
    LockOrientationToPortrait(context = context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (isError && !viewModel.internetState.value){
                Image(
                    painter = painterResource(id = R.drawable.internet_off),
                    contentDescription = manga?.title,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
            }else {
                Image(
                    painter = painter,
                    contentDescription = manga?.title,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxSize()
                        .let {
                            if (isLoading || isError) it.shimmerAnimationEffect() else it
                        }
                )
            }

            // Back Button
            IconButton(
                onClick = {
                    manga?.let { viewModel.updateMangaDetails(it) }
                    navController.popBackStack()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.Black.copy(alpha = 0.6f), shape = CircleShape)
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
            }
        }


        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Manga Title
            manga?.title?.let {
                Text(
                    text = it,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoChip(text = "⭐ ${manga?.score ?: "N/A"}")
                InfoChip(text = "🔥 Popularity: ${manga?.popularity ?: "N/A"}")
            }

            Spacer(modifier = Modifier.height(8.dp))


            manga?.year?.let {
                Text(
                    text = "📅 Published: ${Utils.timestampToReadableDate(it)}",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            manga?.category?.let {
                Text(
                    text = "Category: $it",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                manga?.let { mangaItem ->
                    ActionButton(
                        icon = if (mangaItem.fav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        text = if (mangaItem.fav) "Unfavorite" else "Favorite"
                    ) {
                        viewModel.updateManga(mangaItem.copy(fav = !mangaItem.fav))
                    }

                    ActionButton(
                        icon = if (mangaItem.read) Icons.Default.CheckCircle else Icons.Default.Check,
                        text = if (mangaItem.read) "Read" else "Mark as Read"
                    ) {
                        viewModel.updateManga(mangaItem.copy(read = !mangaItem.read))
                    }
                }
            }
        }
    }
}

// UI Components
@Composable
fun InfoChip(text: String) {
    Box(
        modifier = Modifier
            .background(Color.DarkGray, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(text, color = Color.White, fontSize = 14.sp)
    }
}

@Composable
fun ActionButton(icon: ImageVector, text: String, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Icon(icon, contentDescription = text, tint = Color.White, modifier = Modifier.size(32.dp))
        Text(text, color = Color.White, fontSize = 14.sp)
    }
}

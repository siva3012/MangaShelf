package com.example.managshelf.ui.uiScreens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.example.managshelf.R
import com.example.managshelf.data.database.MangaEntity
import com.example.managshelf.ui.common.Utils
import com.example.managshelf.ui.uiScreens.viewmodel.MangaViewModel
import kotlinx.coroutines.delay

@Composable
fun MangaCard(viewModel: MangaViewModel,manga : MangaEntity,onCardClick :()-> Unit,onFavClick : (Boolean)-> Unit ,onReadClick :(Boolean) -> Unit
) {
    val context = LocalContext.current
    var retryTrigger by remember { mutableStateOf(0) }

    val painter = rememberAsyncImagePainter(
        model = "${manga.imageUrl}?reload=$retryTrigger",
    )

    val isLoading = painter.state is AsyncImagePainter.State.Loading
    val isError = painter.state is AsyncImagePainter.State.Error

    LaunchedEffect(isError) {
        if (isError) {
            delay(5000)
            retryTrigger++
        }
    }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onCardClick() }
            .border(2.dp, Color.White, RoundedCornerShape(12.dp)),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(modifier = Modifier.background(color = Color.Black)) {
            if (isError && !viewModel.internetState.value){
                Image(
                    painter = painterResource(id = R.drawable.internet_off),
                    contentDescription = manga.title,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )
            }
            else {
                Image(
                    painter = painter,
                    contentDescription = manga.title,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .let {
                            if (isLoading || isError) it.shimmerAnimationEffect() else it
                        }
                )
            }

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = manga.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                )
                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Year: ${Utils.timestampToYear(manga.year)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.LightGray
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "⭐ ${manga.score}",
                        fontWeight = FontWeight.Bold,
                        color = Color.Yellow
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "🔥 ${manga.popularity} Users",
                        fontWeight = FontWeight.Bold,
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            onFavClick(!manga.fav)
                            Toast.makeText(
                                context,
                                if (manga.fav) "Added to Favorite" else "Removed from Favorites",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (manga.fav) Color(
                                0xFFFF4081
                            ) else Color.Gray
                        )
                    ) {
                        Text(
                            text = if (manga.fav) "\uD83E\uDD0D Favorite" else "🤍 Favorite",
                            color = Color.White
                        )
                    }
                    Button(
                        onClick = {
                            onReadClick(!manga.read)
                            Toast.makeText(
                                context,
                                if (manga.read) "Marked as Read" else "Marked as Unread",
                                Toast.LENGTH_SHORT
                            ).show()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (manga.read) Color(
                                0xFF4CAF50
                            ) else Color.Gray
                        )
                    ) {
                        Text(text = if (manga.read) "✔  Read" else "📘 Read", color = Color.White)
                    }
                }
            }
        }
    }
}


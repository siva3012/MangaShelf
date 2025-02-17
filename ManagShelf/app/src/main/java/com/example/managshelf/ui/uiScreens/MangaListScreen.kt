package com.example.managshelf.ui.uiScreens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.managshelf.ui.common.SortOption
import com.example.managshelf.ui.common.Utils
import com.example.managshelf.ui.uiScreens.viewmodel.MangaViewModel
import kotlinx.coroutines.launch


@Composable
fun MangaListScreen(navController: NavController, viewModel: MangaViewModel) {
    val dataListState = viewModel.dataList.collectAsState()
    val showDialog = viewModel.showDialog.collectAsState()
    val sortedList by remember(dataListState.value) {
        derivedStateOf { dataListState.value }
    }
    val isLoading by viewModel.isLoading.collectAsState()

    var distinctYears by remember { mutableStateOf(emptyList<Int>()) }
    val context = LocalContext.current
    LaunchedEffect(sortedList) {
        distinctYears = sortedList.map { Utils.timestampToYear(it.year) }.distinct().sorted()
    }

    val selectedYear = remember { mutableStateOf(distinctYears.firstOrNull() ?: 0) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val showFilter = remember { mutableStateOf(false) }
    val showYearBar = remember { mutableStateOf(viewModel.selectOption == SortOption.YEAR_ASC) }
    LockOrientationToPortrait(context = context)

    Box(modifier = Modifier.background(color = Color.Black)) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(viewModel) {
                if (!isLoading) {
                    showFilter.value = !showFilter.value
                }
            }

            if (showFilter.value) {
                SideBar(viewModel, onDismiss = { showFilter.value = false }, onSortSelected = { option ->
                    showFilter.value = false
                    showYearBar.value = option==SortOption.YEAR_ASC
                    viewModel.setSelectedOption(option)
                })
            }

            if (showYearBar.value) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .height(40.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(if (isLoading) List(4) { it + 1 } else distinctYears) { year ->
                        if (isLoading){
                            YearShimmer()
                        }else {
                            YearTab(
                                year = year.toString(),
                                isSelected = year == selectedYear.value,
                                onClick = {
                                    selectedYear.value = year
                                    coroutineScope.launch {
                                        val index =
                                            sortedList.indexOfFirst { Utils.timestampToYear(it.year) == year }
                                        if (index != -1) {
                                            listState.animateScrollToItem(index)
                                        }
                                    }
                                }
                            )
                        }
                    }
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                itemsIndexed(sortedList) { index, manga ->
                    if (isLoading) {
                        MangaCardshimmer()
                    } else {
                        MangaCard(
                            manga = manga,
                            viewModel = viewModel,
                            onCardClick = {
                                viewModel.selectManga(manga)
                                navController.navigate("mangaDetail")
                            },
                            onFavClick = {
                                manga.fav = !manga.fav
                                viewModel.updateManga(manga)},
                            onReadClick = {
                                manga.read = !manga.read
                                viewModel.updateManga(manga)
                            }
                        )
                    }
                }
            }
        }
    }

    val firstVisibleItemInfo by remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.firstOrNull() }
    }

    LaunchedEffect(firstVisibleItemInfo) {
        firstVisibleItemInfo?.let { visibleItemInfo ->
            val manga = sortedList.getOrNull(visibleItemInfo.index)
            manga?.let {
                val newYear = Utils.timestampToYear(it.year)
                if (newYear != selectedYear.value) {
                    selectedYear.value = newYear
                }
            }
        }
    }
    if (showDialog.value){
        NoInternetDialog(showDialog = true, onDismiss = {
            val activity = context as? Activity
            activity?.finish()
            viewModel.showDialog.value=false }, context = context)
    }

}

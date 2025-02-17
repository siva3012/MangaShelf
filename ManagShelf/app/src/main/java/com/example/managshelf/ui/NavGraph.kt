package com.example.managshelf.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.managshelf.ui.uiScreens.MangaDetailScreen
import com.example.managshelf.ui.uiScreens.MangaListScreen
import com.example.managshelf.ui.uiScreens.viewmodel.MangaViewModel

@Composable
fun NavGraph(navController: NavHostController, mangaViewModel: MangaViewModel) {
    NavHost(navController = navController, startDestination = "mangaList") {
        composable("mangaList") {
            MangaListScreen(navController, mangaViewModel)
        }
        composable("mangaDetail") {
            MangaDetailScreen(navController,mangaViewModel)
        }
    }
}
package com.example.managshelf

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.managshelf.ui.MyApplicationTheme
import com.example.managshelf.ui.NavGraph
import com.example.managshelf.ui.uiScreens.MangaListScreen
import com.example.managshelf.ui.uiScreens.viewmodel.MangaViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: MangaViewModel by lazy {
        ViewModelProvider(this)[MangaViewModel::class.java]
    }
    private var navController: NavHostController? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    navController= rememberNavController()
                    NavGraph(navController = navController!!, mangaViewModel = viewModel)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.unregisterReceiver()
    }
}

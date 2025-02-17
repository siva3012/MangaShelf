package com.example.managshelf.ui.uiScreens

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalConfiguration

@Composable
fun LockOrientationToPortrait(context: Context) {
    val configuration = LocalConfiguration.current
    LaunchedEffect(configuration.orientation) {

        val currentActivity = context as? Activity
        currentActivity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }
}
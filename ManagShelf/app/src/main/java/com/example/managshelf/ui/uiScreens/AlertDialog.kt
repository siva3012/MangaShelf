package com.example.managshelf.ui.uiScreens

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun NoInternetDialog(showDialog: Boolean, onDismiss: () -> Unit, context: Context) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                onDismiss() },
            title = { Text(text = "No Internet Connection") },
            text = { Text(text = "Please turn on Wi-Fi or Mobile Data.") },
            confirmButton = {
                Button(onClick = {
                    val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
                    context.startActivity(intent)
                }) {
                    Text("Turn On")
                }
            }
        )
    }
}
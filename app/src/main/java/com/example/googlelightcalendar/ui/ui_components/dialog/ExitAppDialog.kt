package com.example.googlelightcalendar.ui.ui_components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ExitAppDialog(
    userResponse: (Boolean) -> Unit,
) {
    AlertDialog(
        title = {
            Text("Exit Application")
        },
        onDismissRequest = {
            userResponse(false)
        },
        confirmButton = {
            Button(onClick = { userResponse(false) }) {
                Text(text = "Cancel")
            }
        },
        dismissButton = {
            Button(onClick = { userResponse(true) }) {
                Text(text = "Exit")
            }
        }
    )

}
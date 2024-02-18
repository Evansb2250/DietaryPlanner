package com.example.chooseu.ui.ui_components.dialog

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalFocusManager

@Composable
fun ErrorDialog(
    title: String,
    error: String,
    onDismiss: () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    GenericAlertDialog(
        title = {
            Text(
                text = title,
            )
        },
        text = {
            Text(
                text = error,
            )
        },
        confirmButton = {
            Button(
                onClick = {
                    //clear focus on previous screen
                    focusManager.clearFocus(true)

                    onDismiss()
                },
            ) {
                Text("Retry")
            }
        }
    )
}
package com.example.googlelightcalendar.ui_components.dialog

import androidx.compose.runtime.Composable


@Composable
fun ToBeImplementedDialog(
    action: () -> Unit = {}
){
    ErrorAlertDialog(
        title = "Feature In Progress",
        error = "feature isn't implemnented",
        onDismiss = action
    )
}


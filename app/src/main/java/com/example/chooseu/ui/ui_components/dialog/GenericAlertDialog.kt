package com.example.chooseu.ui.ui_components.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    showBackground = true
)
@Composable
fun GenericAlertDialog(
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    text: @Composable () -> Unit = {},
    icon: @Composable () -> Unit = {},
    confirmButton: @Composable () -> Unit = {},
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {  },
        confirmButton = confirmButton,
        icon = icon,
        title = title,
        text = text,
        tonalElevation = AlertDialogDefaults.TonalElevation,
    )
}
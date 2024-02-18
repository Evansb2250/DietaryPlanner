package com.example.chooseu.ui.ui_components.dialog

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.window.Dialog


@Composable
fun LoadingDialog(){
    Dialog(
        onDismissRequest = { /*TODO*/ },
        content = {
            CircularProgressIndicator()
        }
    )
}
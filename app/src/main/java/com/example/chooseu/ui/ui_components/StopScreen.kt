package com.example.chooseu.ui.ui_components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.chooseu.R
import com.example.chooseu.ui.theme.appColor

@Composable
fun ScreenUnavailable(){
    Column(
        modifier = Modifier
            .background(appColor)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(
                id = R.drawable.traffic_sign_icon,
            ),
            contentDescription = "",
        )
        Text(
            color = Color.White,
            text = "Page not createad",
        )
    }
}
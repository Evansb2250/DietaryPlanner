package com.example.googlelightcalendar.ui_components.custom_column

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.googlelightcalendar.screens.loginScreen.sidePadding
import com.example.googlelightcalendar.ui.theme.appColor

@Composable
fun AppColumnContainer(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        containerColor = appColor,
    ) { it ->
        Spacer(
            modifier = Modifier.size(20.dp)
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(
                    horizontal = sidePadding
                )
                .background(
                    color = appColor,
                ),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement,
        ){
            Spacer(
                modifier = Modifier.size(20.dp)
            )
            content()
        }
    }

}
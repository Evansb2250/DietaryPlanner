package com.example.googlelightcalendar.ui.ui_components.divider

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CustomDividerText(
    textColor: Color = Color.White
){
    Row(
        modifier = Modifier
            .fillMaxWidth()

    ) {
        Divider(
            modifier = Modifier
                .padding(top = 10.dp)
                .weight(1f)
        )
        Text(
            modifier = Modifier
                .padding(
                    horizontal = 10.dp
                )
                .wrapContentWidth(),
            text = "Or",
            color = textColor,
            textAlign = TextAlign.Center
        )
        Divider(
            modifier = Modifier
                .padding(top = 10.dp)
                .weight(1f)
        )
    }
}
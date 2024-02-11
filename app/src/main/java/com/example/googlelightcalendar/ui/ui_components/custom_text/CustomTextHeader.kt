package com.example.googlelightcalendar.ui.ui_components.custom_text

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

@Composable
fun CustomTextHeader(
    modifier: Modifier,
    text: String,
) {
    Text(
        modifier = modifier
            .fillMaxWidth(),
        text = text,
        textAlign = TextAlign.Center,
        fontSize = TextUnit(
            value = 32f,
            type = TextUnitType.Sp
        )
    )
}

package com.example.googlelightcalendar.ui_components.buttons

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

@Composable
fun StandardButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    Button(
        modifier = modifier
            .height(
                height = 52.dp
            )
            .fillMaxWidth(),
        shape = shape,
        onClick = onClick,
        enabled = enabled
    ) {
        Text(
            text = text
        )
    }

}

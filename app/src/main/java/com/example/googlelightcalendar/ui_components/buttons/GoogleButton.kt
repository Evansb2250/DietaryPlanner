package com.example.googlelightcalendar.ui_components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.googlelightcalendar.R

@Composable
fun GoogleButton(
    onClick: () -> Unit,
    shape: Shape = RoundedCornerShape(8.dp)
) {
    OutlinedButton(
        shape = shape,
        modifier = Modifier
            .fillMaxWidth()
            .height(
                height = 52.dp
            ),
        onClick = onClick,
    ) {
        Image(
            painter = painterResource(id = R.drawable.gmail_icon),
            contentDescription = "Google Icon",
            modifier = Modifier.padding(
                horizontal = 10.dp,
            )
        )
        Text(
            text = "Sign up with Google",
            color = Color.White,
        )
    }
}

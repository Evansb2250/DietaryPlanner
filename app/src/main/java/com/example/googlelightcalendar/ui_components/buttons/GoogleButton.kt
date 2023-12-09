package com.example.googlelightcalendar.ui_components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.googlelightcalendar.R

@Composable
fun GoogleButton(
    onClick: () -> Unit,
){
    OutlinedButton(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth(),
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

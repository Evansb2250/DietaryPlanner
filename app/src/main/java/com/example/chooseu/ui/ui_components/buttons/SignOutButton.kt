package com.example.chooseu.ui.ui_components.buttons

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chooseu.R
import com.example.chooseu.common.sidePadding

@Composable
fun SignOutButton(
    onSignOut: () -> Unit = {},
) {
    OutlinedButton(
        modifier = Modifier
            .height(
                52.dp,
            )
            .fillMaxWidth()
            .padding(
                horizontal = sidePadding
            ),
        onClick = onSignOut
    ) {
        Image(
            modifier = Modifier.padding(
                horizontal = 4.dp,
            ),
            painter = painterResource(
                id = R.drawable.exit_icon,
            ),
            contentDescription = ""
        )
        Text(
            modifier = Modifier
                .align(
                    alignment = Alignment.CenterVertically
                ),
            color = Color.White,
            text = "Sign Out",
        )
    }
}
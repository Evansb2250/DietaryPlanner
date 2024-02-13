package com.example.googlelightcalendar.ui.ui_components.custom_layout

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun CustomRowLayout(
    leadingIcon: @Composable () -> Unit = {},
    title: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    enable: Boolean = true,
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.weight(
                1f,
            ),
            contentAlignment = Alignment.Center,
        ) {
            leadingIcon()
        }
        Box(
            modifier = Modifier
                .weight(
                    3f,
                )
                .clickable(
                    enabled = enable
                ) {
                    onClick()
                },
        ) {
            title()
        }
        Box(
            modifier = Modifier
                .weight(
                    1f,
                )
                .clickable(
                    enabled = enable
                ) {
                    onClick()
                },
            contentAlignment = Alignment.CenterEnd,
        ) {
            trailingIcon()
        }
    }
}
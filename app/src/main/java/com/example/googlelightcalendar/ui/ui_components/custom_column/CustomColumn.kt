package com.example.googlelightcalendar.ui.ui_components.custom_column

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.googlelightcalendar.common.sidePadding
import com.example.googlelightcalendar.ui.theme.appColor

@Composable
fun AppColumnContainer(
    modifier: Modifier = Modifier,
    disableBackPress: Boolean = true,
    containerColor: Color = appColor,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: @Composable ColumnScope.() -> Unit
) {

    val focusManager = LocalFocusManager.current
    //By default user can't pop the stack.
    if (disableBackPress) {
        BackHandler {}
    }

    Scaffold(
        modifier = Modifier
            .clickable(
                interactionSource = null,
                indication = null,
            ) { focusManager.clearFocus() }
            .fillMaxWidth(),
        containerColor = containerColor,
    ) { padding ->
        Spacer(
            modifier = Modifier.size(20.dp)
        )
        Column(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxSize()
                .padding(
                    padding
                )
                .background(
                    color = containerColor,
                ),
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement,
        ) {
            Spacer(
                modifier = Modifier.size(20.dp)
            )
            content()
        }
    }

}
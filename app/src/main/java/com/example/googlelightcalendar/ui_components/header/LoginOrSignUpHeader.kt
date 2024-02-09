package com.example.googlelightcalendar.ui_components.header

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.googlelightcalendar.R

@Composable
fun LoginOrSignUpTabAndHeader(
    onShowLoginScreen: () -> Unit,
    onShowRegistrationScreen: () -> Unit,
) {
    val tabIndex by remember {
        derivedStateOf { mutableStateOf(0) }
    }

    val tabs = listOf("Login", "Sign Up")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black),
    ) {
        Image(
            painter = painterResource(id = R.drawable.chooseuloginlogo),
            contentDescription = "",
            modifier = Modifier.fillMaxWidth()
        )

        TabRow(
            modifier = Modifier.align(
                Alignment.CenterHorizontally
            ),
            containerColor = Color.Black,
            selectedTabIndex = tabIndex.value,
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex.value == index,
                    onClick = { tabIndex.value = index },
                    selectedContentColor = Color.White
                )
            }
        }

        when (tabIndex.value) {
            0 -> {
                onShowLoginScreen()
            }

            else -> {
                onShowRegistrationScreen()
            }
        }
    }
}

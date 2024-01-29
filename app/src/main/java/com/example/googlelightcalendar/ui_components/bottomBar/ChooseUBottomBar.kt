package com.example.googlelightcalendar.ui_components.bottomBar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.googlelightcalendar.navigation.components.MainScreenNavigation
import com.example.googlelightcalendar.navigation.components.screens
import com.example.googlelightcalendar.ui.theme.yellowMain

@Composable
fun ChooseUBottomBar(
    tabPosition: Int,
    onClick: (
        tabIndex: Int,
        item: MainScreenNavigation,
    ) -> Unit = { _, _ -> },
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        screens.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = tabPosition == index,
                onClick = { onClick(index, item) },
                icon = {
                    Icon(
                        painterResource(
                            id = item.icon,
                        ), contentDescription = null
                    )
                }, colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = yellowMain, indicatorColor = Color.White
                )
            )
        }
    }
}
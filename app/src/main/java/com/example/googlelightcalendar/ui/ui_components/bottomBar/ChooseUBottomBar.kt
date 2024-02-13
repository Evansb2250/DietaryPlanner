package com.example.googlelightcalendar.ui.ui_components.bottomBar

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.googlelightcalendar.navigation.components.destinations.BottomNavBarDestinations
import com.example.googlelightcalendar.ui.theme.yellowMain

@Composable
fun ChooseUBottomBar(
    tabs: List<BottomNavBarDestinations>,
    tabPosition: Int,
    onClick: (
        item: BottomNavBarDestinations,
    ) -> Unit = {},
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        tabs.forEach{  item ->
            NavigationBarItem(
                selected = tabPosition == item.routeId,
                onClick = { onClick(item) },
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
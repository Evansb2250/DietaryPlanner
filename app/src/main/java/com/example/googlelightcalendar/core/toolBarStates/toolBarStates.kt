package com.example.googlelightcalendar.core.toolBarStates

import androidx.annotation.DrawableRes
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.navigation.components.MainScreenNavigation

sealed class ToolBarState(
    val leadingIcon: LeadingIcon,
    val headline: String = "",
    val trailingIcon: TrailingIcon,
) {
    data class Navigated(
        val title: String,
    ) : ToolBarState(
        leadingIcon = LeadingIcon(
            R.drawable.back_arrow, clickable = true
        ),
        headline = title,
        trailingIcon = TrailingIcon(
            drawable = R.drawable.notification_bell,
            destinations = MainScreenNavigation.NotificationScreen,
        ),
    )

    data class Home(
        val showNotificationBadge: Boolean = false,
        var showTrailingIcon: Boolean = true,
    ) : ToolBarState(
        leadingIcon = LeadingIcon(
            R.drawable.logo3,
            clickable = false,
        ),
        trailingIcon = TrailingIcon(
            drawable = R.drawable.notification_bell,
            destinations = MainScreenNavigation.NotificationScreen,
        ),
    )
}

data class LeadingIcon(
    @DrawableRes val drawable: Int,
    val clickable: Boolean,
)

data class Headline(
    val string: String,
)

data class TrailingIcon(
    @DrawableRes val drawable: Int,
    val destinations: MainScreenNavigation,
)
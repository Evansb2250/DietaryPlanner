package com.example.chooseu.core.toolbar_states

import androidx.annotation.DrawableRes
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.destinations.ProfileDestinations
import com.example.chooseu.R

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
            destinations = GeneralDestinations.Notification,
        ),
    )

    data class Home(
        var showTrailingIcon: Boolean = true,
    ) : ToolBarState(
        leadingIcon = LeadingIcon(
            R.drawable.logo3,
            clickable = false,
        ),
        trailingIcon = TrailingIcon(
            drawable = R.drawable.notification_bell,
            destinations = ProfileDestinations.NotificationScreen,
        ),
    )
}

data class LeadingIcon(
    @DrawableRes val drawable: Int,
    val clickable: Boolean,
)
data class TrailingIcon(
    @DrawableRes val drawable: Int,
    val destinations: GeneralDestinations,
)
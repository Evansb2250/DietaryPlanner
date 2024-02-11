package com.example.googlelightcalendar.core.profile_screen.states

import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.navigation.components.destinations.GeneralDestinations
import com.example.googlelightcalendar.navigation.components.destinations.ProfileDestinations

sealed class ProfileScreenStates {
    object ProfilePage : ProfileScreenStates(){

        data class ProfileOptions(
            val leadingIconId: Int,
            val text: String,
            val trailingIconId: Int,
            val destination: GeneralDestinations,
        )


        val items = listOf(
            ProfileOptions(
                leadingIconId = R.drawable.profile_icon_2,
                text = "Account",
                trailingIconId = R.drawable.right_arrow,
                destination = ProfileDestinations.Account
            ),

            ProfileOptions(
                leadingIconId = R.drawable.notification_icon_2,
                text = "Notification",
                trailingIconId = R.drawable.right_arrow,
                destination = ProfileDestinations.NotificationScreen
            ),

            ProfileOptions(
                leadingIconId = R.drawable.calendar_icon_2,
                text = "Calendar",
                trailingIconId = R.drawable.right_arrow,
                destination = ProfileDestinations.Calendar,
            ),

            ProfileOptions(
                leadingIconId = R.drawable.lock_icon,
                text = "Terms of Service",
                trailingIconId = R.drawable.right_arrow,
                destination = ProfileDestinations.TOS,
            )
        )
    }
}
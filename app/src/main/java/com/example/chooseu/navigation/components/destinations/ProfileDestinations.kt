package com.example.chooseu.navigation.components.destinations

sealed class ProfileDestinations {

    object Account : GeneralDestinations(
        destination = "profile/account"
    )

    object NotificationScreen : GeneralDestinations(
        destination = "profile/notifications"
    )

    object Calendar : GeneralDestinations(
        destination = "profile/calendar"
    )

    object TOS : GeneralDestinations(
        destination = "profile/tos"
    )
}
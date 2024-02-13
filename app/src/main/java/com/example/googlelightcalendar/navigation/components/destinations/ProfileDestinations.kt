package com.example.googlelightcalendar.navigation.components.destinations

sealed class ProfileDestinations {

    object Account : GeneralDestinations(
        destination = "profile/account/{userId}"
    )

    object NotificationScreen : GeneralDestinations(
        destination = "profile/notifications/{userId}"
    )

    object Calendar : GeneralDestinations(
        destination = "profile/calendar/{userId}"
    )

    object TOS : GeneralDestinations(
        destination = "profile/tos/"
    )
}
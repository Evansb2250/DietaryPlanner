package com.example.chooseu.navigation.components.destinations

sealed class ProfileDestinations {

    object Account : GeneralDestinations(
        destination = "profile/account/{userId}"
    )

    object NotificationScreen : GeneralDestinations(
        destination = "profile/notifications/{userId}"
    )

    object Calendar_settings : GeneralDestinations(
        destination = "profile/calendar_settings/{userId}"
    )

    object TOS : GeneralDestinations(
        destination = "profile/tos/{userId}"
    )
}
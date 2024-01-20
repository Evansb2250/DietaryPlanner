package com.example.googlelightcalendar.navigation.components

import androidx.annotation.DrawableRes
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.navigation.components.ProfileRoutes.*

sealed class NavigationDestinations(
    override val destination: String,
    override val arguments: List<String> = emptyList()
) : Navigation {
    object loginScreen : NavigationDestinations(
        destination = "loginScreen",
    )

    object RegistrationPath : NavigationDestinations(
        destination = "registerPaths/{email}",
    )

    object RegisterScreen : NavigationDestinations(
        destination = "registration/{email}",
    )

    object RegisterPhysicalScreen : NavigationDestinations(
        destination = "registerPhysicalScreen/",
    )

    object RegisterGoalsScreen : NavigationDestinations(
        destination = "registerGoalScreen/",
    )

    object MainScreen : NavigationDestinations(
        destination = "mainScreen/{userId}",
    )
}

abstract class MainScreenNavigation(

    override val destination: String,
    override val arguments: List<String> = emptyList()
) : NavigationDestinations(
    destination = destination,
    arguments = arguments,
) {
    abstract @get:DrawableRes
    val icon: Int
    abstract val iconDescription: String

    object Home : MainScreenNavigation(
        destination = "homeScreen/",
    ) {
        override val icon: Int = R.drawable.home_icon
        override val iconDescription: String = ""
    }

    object Diary : MainScreenNavigation(
        destination = "Diary/",
    ) {
        override val icon: Int = R.drawable.food_icon
        override val iconDescription: String = ""
    }

    object Calendar : MainScreenNavigation(
        destination = "calendar/",
    ) {
        override val icon: Int = R.drawable.calendar_icon
        override val iconDescription: String = ""
    }

    companion object {
        val screens = listOf(
            Home,
            Diary,
            Calendar,
            Profile,
        )

    }
}


sealed class ProfileRoutes {
    object Profile : MainScreenNavigation(
        destination = "profile/",
    ) {
        override val icon: Int = R.drawable.profile_icon
        override val iconDescription: String = ""
    }

    object Account : NavigationDestinations(
        destination = "profile/account/{userId}"
    )

    object Notifications : NavigationDestinations(
        destination = "profile/notifications/{userId}"
    )

    object Calendar : NavigationDestinations(
        destination = "profile/calendar/{userId}"
    )

    object TOS : NavigationDestinations(
        destination = "profile/tos/"
    )
}
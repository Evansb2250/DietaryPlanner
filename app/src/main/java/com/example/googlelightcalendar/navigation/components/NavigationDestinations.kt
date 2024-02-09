package com.example.googlelightcalendar.navigation.components

import androidx.annotation.DrawableRes
import com.example.googlelightcalendar.R

sealed class NavigationDestinations(
    override val destination: String,
    override val arguments: List<String> = emptyList()
) : Navigation {

    /**
     * Contains both login and registration screen wrapped in a container.
     */
    object OnAppStartUp: NavigationDestinations(
        destination = "onAppStartUp/{screenType}",
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

sealed class MainScreenNavigations(
    override val destination: String,
    override val arguments: List<String> = emptyList()
) : NavigationDestinations(
    destination,
    arguments
){
    object NotificationScreen : MainScreenNavigations(
        destination = "NotificationScreen/{userId}",
    )
}

sealed class ProfileRoutes {

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

sealed class BottomNavBarDestinations(
    val routeId: Int,
    override val destination: String,
    override val arguments: List<String> = emptyList()
) : MainScreenNavigations(
    destination = destination,
    arguments = arguments,
) {
    abstract @get:DrawableRes
    val icon: Int
    abstract val iconDescription: String

    object Home : BottomNavBarDestinations(
        routeId = 0,
        destination = "homeScreen/",
    ) {
        override val icon: Int = R.drawable.home_icon
        override val iconDescription: String = ""
    }

    object Diary : BottomNavBarDestinations(
        routeId = 1,
        destination = "Diary/",
    ) {
        override val icon: Int = R.drawable.food_icon
        override val iconDescription: String = ""
    }

    object Calendar : BottomNavBarDestinations(
        routeId = 2,
        destination = "calendar/",
    ) {
        override val icon: Int = R.drawable.calendar_icon
        override val iconDescription: String = ""
    }

    object Profile : BottomNavBarDestinations(
        routeId = 3,
        destination = "profile/",
    ) {
        override val icon: Int = R.drawable.profile_icon
        override val iconDescription: String = ""
    }
}



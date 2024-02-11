package com.example.googlelightcalendar.navigation.components.destinations

import androidx.annotation.DrawableRes
import com.example.googlelightcalendar.R

sealed class BottomNavBarDestinations(
    val routeId: Int,
    override val destination: String,
    override val arguments: List<String> = emptyList()
) : GeneralDestinations(
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
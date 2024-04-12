package com.example.chooseu.navigation.components.destinations

import androidx.annotation.DrawableRes
import com.example.chooseu.R
import com.example.chooseu.navigation.components.destinations.destinationArguments.DiaryArgs

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
        destination = "homeScreen/{userId}",
    ) {
        override val icon: Int = R.drawable.home_icon
        override val iconDescription: String = ""
    }

    object Diary : BottomNavBarDestinations(
        routeId = 1,
        destination = "Diary/{userId}/{${DiaryArgs.LONG_DATE}}",
    ) {
        override val icon: Int = R.drawable.food_icon
        override val iconDescription: String = ""
    }

    object Calendar : BottomNavBarDestinations(
        routeId = 2,
        destination = "calendar/{userId}",
    ) {
        override val icon: Int = R.drawable.calendar_icon
        override val iconDescription: String = ""
    }

    object Profile : BottomNavBarDestinations(
        routeId = 3,
        destination = "profile/{userId}",
    ) {
        override val icon: Int = R.drawable.profile_icon
        override val iconDescription: String = ""
    }
}
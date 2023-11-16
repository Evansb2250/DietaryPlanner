package com.example.googlelightcalendar.navigation.components

object NavigationDestinations {

    val loginScreen = object : Navigation {
        override val destination: String
            get() = "loginScreen"
        override val arguments: List<String>
            get() = emptyList()

    }

    val registerScreen = object : Navigation{
        override val destination: String
            get() = "navigation"
        override val arguments: List<String>
            get() = emptyList()

    }
}
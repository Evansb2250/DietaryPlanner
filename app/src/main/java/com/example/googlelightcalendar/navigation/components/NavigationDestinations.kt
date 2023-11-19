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
            get() = "registration/{email}"
        override val arguments: List<String>
            get() = emptyList()

    }


    val experiment = object : Navigation{
        override val destination: String
            get() = "experiment/{email}/{id}/{name}"
        override val arguments: List<String>
            get() = emptyList()

    }
}


object NavigationBuilder {
    fun buildDestination(navigation: Navigation, replacements: Map<String, String>): Navigation {
        var destination = navigation.destination
        replacements.forEach { (key, value) ->
            destination = destination.replace("{$key}", value)
        }
        return object : Navigation{
            override val destination: String
                get() = destination
            override val arguments: List<String>
                get() = replacements.map { it.value }

        }
    }
}
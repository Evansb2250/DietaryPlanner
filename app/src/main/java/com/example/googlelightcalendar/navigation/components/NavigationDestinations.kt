package com.example.googlelightcalendar.navigation.components

object NavigationDestinations {

    val loginScreen = object : Navigation {
        override val destination: String
            get() = "loginScreen"
        override val arguments: List<String>
            get() = emptyList()

    }

    object RegistrationPath : Navigation {
        override val destination: String
            get() = "registerPaths/{email}"
        override val arguments: List<String>
            get() = emptyList()
    }

    val registerScreen = object : Navigation {
        override val destination: String
            get() = "registration/{email}"
        override val arguments: List<String>
            get() = emptyList()

    }
    val registerPhysicalScreen = object : Navigation {
        override val destination: String
            get() = "registerPhysicalScreen/"
        override val arguments: List<String>
            get() = emptyList()

    }

    val registerGoalsScreen = object : Navigation {
        override val destination: String
            get() = "registerGoalScreen/"
        override val arguments: List<String>
            get() = emptyList()

    }

    val registerConfirmationScreen = object : Navigation {
        override val destination: String
            get() = "registerConfirmationScreen/"
        override val arguments: List<String>
            get() = emptyList()

    }

    val HomeScreen = object : Navigation{
        override val destination: String
            get() = "homeScreen/{userId}"
        override val arguments: List<String>
            get() = emptyList()

    }

    val experiment = object : Navigation {
        override val destination: String
            get() = "experiment/{email}/{id}/{name}"
        override val arguments: List<String>
            get() = emptyList()

    }
}


object NavigationBuilder {
    fun buildDestination(
        navigation: Navigation,
        parameters: Map<String, String>,
    ): Navigation {
        //Makes the path mutable
        var destination = navigation.destination

        //checks the list of paramters
        parameters.forEach { (key, value) ->
            destination = destination.replace("{$key}", value)
        }
        return object : Navigation {
            override val destination: String
                get() = destination
            override val arguments: List<String>
                get() = parameters.map { it.value }

        }
    }
}
package com.example.chooseu.navigation.components.destinations

import com.example.chooseu.navigation.components.Navigation

sealed class GeneralDestinations(
    override val destination: String,
    override val arguments: List<String> = emptyList()
) : Navigation {

    /**
     * Contains both login and registration screen wrapped in a container.
     */
    object OnAppStartUpDestination: GeneralDestinations(
        destination = "onAppStartUp/{screenType}",
    )

    object RegistrationDestinations : GeneralDestinations(
        destination = "registerPaths/{email}",
    )

    object RegisterDetailsDestination : GeneralDestinations(
        destination = "registerPhysicalScreen",
    )

    object RegisterGoalsDestination : GeneralDestinations(
        destination = "registerGoalScreen",
    )

    object MainScreenDestinations : GeneralDestinations(
        destination = "mainScreen",
    )

    object NotificationDestination : GeneralDestinations(
        destination = "NotificationScreen/{GeneralDestinations}",
    )

    object FoodSearchDestination : GeneralDestinations(
        destination = " FoodSearchScreen/{day}/{foodType}",
    )
}



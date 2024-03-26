package com.example.chooseu.navigation.components.destinations

import android.net.Uri
import com.example.chooseu.navigation.components.Navigation
import com.example.chooseu.navigation.components.destinations.destinationArguments.DiaryArgs

sealed class GeneralDestinations(
    override val destination: String,
    override val arguments: List<String> = emptyList()
) : Navigation {
    object OnAppStartUpDestination : GeneralDestinations(
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

    object FoodSearchDestination :  GeneralDestinations(
        destination = " FoodSearchScreen/{${DiaryArgs.LONG_DATE}}/{${DiaryArgs.MEAL_TYPE}}",
    )

    object NutritionScreen: GeneralDestinations(
        destination = "NutritionScreen/{foodId}/{date}"
    )
}



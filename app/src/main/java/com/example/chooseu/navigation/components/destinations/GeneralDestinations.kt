package com.example.chooseu.navigation.components.destinations

import com.example.chooseu.navigation.components.Navigation
import com.example.chooseu.navigation.components.destinations.destinationArguments.DiaryArgs

sealed class GeneralDestinations(
    override val destination: String,
    override val arguments: List<String> = emptyList()
) : Navigation {
    object AuthentificationFlow : GeneralDestinations(
        destination = "onAppStartUp/{screenType}",
    )
    object RegistrationFlow : GeneralDestinations(
        destination = "registerPaths/{email}",
    )

    object RegisterDetailsFlow : GeneralDestinations(
        destination = "registerPhysicalScreen",
    )

    object RegisterGoalsFlow : GeneralDestinations(
        destination = "RegisterGoalScreen",
    )

    object MainScreenFlow : GeneralDestinations(
        destination = "MainScreen/{userId}",
    )

    object Notification : GeneralDestinations(
        destination = "NotificationScreen/{GeneralDestinations}",
    )

    object FoodSearchFlow :  GeneralDestinations(
        destination = " FoodSearchScreen/{userId}/{${DiaryArgs.LONG_DATE}}/{${DiaryArgs.MEAL_TYPE}}",
    )

    object Nutrition: GeneralDestinations(
        destination = "NutritionScreen/{userId}/{foodId}/{${DiaryArgs.LONG_DATE.name}}"
    )
}



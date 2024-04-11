package com.example.chooseu.navigation.navgraphs

import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.chooseu.navigation.components.destinations.BottomNavBarDestinations
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.destinations.destinationArguments.DiaryArgs
import com.example.chooseu.ui.screens.calendar.CalendarScreen
import com.example.chooseu.ui.screens.food_diary.DiaryScreen
import com.example.chooseu.ui.screens.food_search.FoodSearchScreen
import com.example.chooseu.ui.screens.home.HomeScreen
import com.example.chooseu.ui.screens.nutrition_screen.NutritionScreen
import com.example.chooseu.ui.screens.profile.ProfileScreen

fun NavBackStackEntry.getUserId(): String = this.arguments?.getString("userId") ?: ""
fun NavBackStackEntry.getLongDate(): Long? =
    this.arguments?.getString(DiaryArgs.LONG_DATE.name)?.toLongOrNull()


fun NavGraphBuilder.MainScreenRoutes(
) {
    composable(
        route = BottomNavBarDestinations.Home.destination
    ) {
        HomeScreen(
            userId = it.getUserId()
        )
    }

    composable(
        route = BottomNavBarDestinations.Diary.destination
    ) {

        DiaryScreen(
            userId = it.getUserId(),
            dateLong = it.getLongDate(),
        )
    }

    composable(
        route = BottomNavBarDestinations.Calendar.destination
    ) {
        CalendarScreen(
            userId = it.getUserId(),
        )
    }

    composable(
        route = BottomNavBarDestinations.Profile.destination
    ) {
        ProfileScreen(
            userId = it.getUserId(),
        )
    }

    ProfileNavGraph()

    composable(
        route = GeneralDestinations.FoodSearchDestination.destination
    ) {
        val title = it.arguments?.getString(DiaryArgs.MEAL_TYPE.name) ?: "cant find"
        FoodSearchScreen(
            title = title,
            dateLong = it.getLongDate() ?: return@composable,
            userId = it.getUserId(),
        )
    }

    composable(
        route = GeneralDestinations.NutritionScreen.destination
    ) {
        val foodId = it.arguments?.getString("foodId") ?: return@composable

        NutritionScreen(
            userId = it.getUserId(),
            foodId = foodId,
        )
    }
}
package com.example.chooseu.navigation.navgraphs

import androidx.activity.compose.BackHandler
import androidx.compose.material3.Scaffold
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.chooseu.core.toolbar_states.ToolBarState
import com.example.chooseu.navigation.components.destinations.BottomNavBarDestinations
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.destinations.destinationArguments.DiaryArgs
import com.example.chooseu.ui.screens.food_diary.DiaryScreen
import com.example.chooseu.ui.screens.food_search.FoodSearchScreen
import com.example.chooseu.ui.screens.nutrition_screen.NutritionScreen
import com.example.chooseu.ui.screens.profile.ProfileScreen
import com.example.chooseu.ui.ui_components.ScreenUnavailable
import com.example.chooseu.ui.ui_components.toolbar.ChooseUToolBar

fun NavGraphBuilder.MainScreenRoutes(
) {
    composable(
        route = BottomNavBarDestinations.Home.destination
    ) {
        Scaffold(
            topBar = {
                ChooseUToolBar(
                    toolBarState = ToolBarState.Home(),
                    navigateBack = { /*TODO*/ },
                    navigateToActionDestination = {}
                )
            }
        ) { it ->
            BackHandler {

            }

            ScreenUnavailable()
        }
    }

    composable(
        route = BottomNavBarDestinations.Diary.destination
    ) {
        val longDate = it.arguments?.getString(DiaryArgs.LONG_DATE.name)?.toLongOrNull()
        DiaryScreen(
            dateLong = longDate,
        )
    }

    composable(
        route = BottomNavBarDestinations.Calendar.destination
    ) {
        Scaffold(
            topBar = {
                ChooseUToolBar(
                    toolBarState = ToolBarState.Home(),
                    navigateBack = { /*TODO*/ },
                    navigateToActionDestination = {}
                )
            }
        ) { it ->
            BackHandler {

            }

            ScreenUnavailable()
        }
    }

    composable(
        route = BottomNavBarDestinations.Profile.destination
    ) {
        ProfileScreen()
    }

    ProfileNavGraph()

    composable(
        route = GeneralDestinations.FoodSearchDestination.destination
    ) {
        val longDate = it.arguments?.getString(DiaryArgs.LONG_DATE.name) ?: "cant find"
        val title = it.arguments?.getString(DiaryArgs.MEAL_TYPE.name) ?: "cant find"
        FoodSearchScreen(
            title = title,
            dateLong = longDate.toLong()
        )
    }

    composable(
        route = GeneralDestinations.NutritionScreen.destination
    ) {
        val foodId = it.arguments?.getString("foodId") ?: return@composable

        NutritionScreen(
            foodId
        )
    }
}
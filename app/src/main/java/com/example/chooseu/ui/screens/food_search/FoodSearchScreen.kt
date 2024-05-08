package com.example.chooseu.ui.screens.food_search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chooseu.core.MealType
import com.example.chooseu.di.VMAssistFactoryModule

@Composable
fun FoodSearchScreen(
    userId: String,
    mealTypeString: String,
    dateLong: Long,
    vm: FoodSearchViewModel = hiltViewModel(
        creationCallback = { factory: VMAssistFactoryModule.FoodSearchFactory ->
            factory.create(
                userId = userId,
                mealType = MealType.buildMealType(mealTypeString),
                date = dateLong,
            )
        }
    )
) {

    FoodSearchScreenContent(
        title = mealTypeString,
        state = vm.state.collectAsStateWithLifecycle().value,
        navigateBackToFoodDiary = vm::navigateBackToFoodDiary,
        searchFoodItem = vm::updateSearchText,
        clearDialog = vm::reset,
        addItem = vm::updateFoodItemList,
        viewNutrientDetails = vm::viewNutrientDetails
    )
}
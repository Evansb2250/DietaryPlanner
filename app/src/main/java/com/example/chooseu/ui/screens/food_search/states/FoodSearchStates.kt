package com.example.chooseu.ui.screens.food_search.states

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.chooseu.ui.screens.nutrition_screen.FoodItem

sealed class FoodSearchStates {
    data class LoggingFoodItem(
        val searchedText: String = "",
        val foodItemsFound: MutableState<List<FoodItem>> = mutableStateOf(emptyList()),
        val loading: Boolean = false,
        val errorState: FoodSearchErrorState = FoodSearchErrorState(),
        val userInput: String = "",
    ) : FoodSearchStates()

    data class FoodSearchErrorState(val isError: Boolean = false, val message: String = "")
}
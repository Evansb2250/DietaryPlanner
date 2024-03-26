package com.example.chooseu.core.diary.states

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.chooseu.core.diary.models.FoodItem

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
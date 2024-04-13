package com.example.chooseu.ui.screens.food_search.states

sealed class FoodItemListActions {
    object INCREMENT : FoodItemListActions()
    object DECREMENT : FoodItemListActions()
}
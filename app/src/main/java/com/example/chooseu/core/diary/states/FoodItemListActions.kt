package com.example.chooseu.core.diary.states

sealed class FoodItemListActions {
    object INCREMENT : FoodItemListActions()
    object DECREMENT : FoodItemListActions()
}
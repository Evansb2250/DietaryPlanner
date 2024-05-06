package com.example.chooseu.core

sealed class MealType(
    open val label: String,
) {

    object Breakfast : MealType(
        label = "Breakfast",
    )

    object Lunch : MealType(
        label = "Lunch",
    )

    object Dinner : MealType(
        label = "Dinner",
    )
}
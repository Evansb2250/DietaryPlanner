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

    companion object {
        fun buildMealType(
            mealTypeString: String
        ): MealType? {
            return when (mealTypeString.lowercase()) {
                Breakfast.label.lowercase() -> Breakfast
                Lunch.label.lowercase() -> Lunch
                Dinner.label.lowercase() -> Dinner
                else -> null
            }
        }
    }
}
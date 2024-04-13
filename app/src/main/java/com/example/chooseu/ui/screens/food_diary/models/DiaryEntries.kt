package com.example.chooseu.ui.screens.food_diary.models

import com.example.chooseu.ui.screens.nutrition_screen.FoodItem


sealed class FoodDiaryItem(
    val typeOfMeal: DiaryEntryTypes,
    val items: List<FoodItem>,
    val calorieCount: Int = 0
)

data class Breakfast(
    val breakfast: DiaryEntryTypes = DiaryEntryTypes.BREAKFAST,
    val breakfastItems: List<FoodItem> = emptyList(),
) : FoodDiaryItem(breakfast, breakfastItems)

data class Lunch(
    val lunch: DiaryEntryTypes = DiaryEntryTypes.LUNCH,
    val lunchItems: List<FoodItem> = emptyList(),
) : FoodDiaryItem(lunch, lunchItems)

data class Dinner(
    val dinner: DiaryEntryTypes = DiaryEntryTypes.DINNER,
    val dinnerItems: List<FoodItem> = emptyList(),
) : FoodDiaryItem(dinner, dinnerItems)
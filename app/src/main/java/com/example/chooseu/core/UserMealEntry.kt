package com.example.chooseu.core

import com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants

/**
 *  Combination of [FoodItem] object and [userId] to connect a user to a meal for a specific day.
 *  @param [foodServingUri] is used to get the nutrition values from the Edamam's Food API for a specific type of service.
 *  @param [totalLipidFat] total fat of a food item retrieved from Edamam's food API.
 *  @param [carbs] must use carbs by difference instead of total carbs due to API limitations
 */
data class UserMealEntry(
    val userId: String,
    val day: Long,
    val foodId: String,
    val foodName: String,
    val mealType: MealType,
    val foodServingUri: String = NutritionConstants.GRAM_URI,
    val servingLabel: String = NutritionConstants.GRAM_LABEL,
    val quantity: Double,
    val protein: Double,
    val carbs: Double,
    val totalLipidFat: Double,
    val totalCalories: Double,
)
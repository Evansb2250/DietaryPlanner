package com.example.chooseu.core

import com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants

data class SavedNutritionValue(
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
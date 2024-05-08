package com.example.chooseu.ui.screens.nutrition_screen

import com.example.chooseu.core.MealType
import com.example.chooseu.core.UserMealEntryRequest
import com.example.chooseu.data.rest.api_service.dtos.foodItemDTO.Measure
import com.example.chooseu.data.rest.api_service.dtos.foodItemDTO.Nutrients
import com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants

data class FoodItem(
    var quantity: Int,
    val foodId: String = "",
    val label: String = "",
    val knownAs: String? = "",
    val nutrients: Nutrients,
    val category: String = "",
    val categoryLabel: String = "",
    val image: String = "",
    val foodContentsLabel: String = "",
    val unitTypes: List<Measure>,
)

fun List<FoodItem>.partitionFoodItemsRecommendations(): Pair<List<FoodItem>, List<FoodItem>> {
    val bestMatches = this.take(3)
    val otherMatches = this.drop(3)
    return Pair(bestMatches, otherMatches)
}


fun FoodItem.toUserMealEntry(
    userId: String,
    date: Long,
    mealType: MealType,
    foodServingUri: String = NutritionConstants.GRAM_URI,
    foodServingLabel: String = NutritionConstants.GRAM_LABEL,
    quantity: Double,
): UserMealEntryRequest =
    UserMealEntryRequest(
        userId = userId,
        date = date,
        foodId = this.foodId,
        foodName = this.label,
        mealType = mealType,
        foodServingUri = foodServingUri,
        servingLabel = foodServingLabel,
        quantity = quantity,
        protein = this.nutrients.protein * quantity,
        carbs = this.nutrients.carbohydrates * quantity,
        totalLipidFat = this.nutrients.fat * quantity,
        totalCalories = this.nutrients.energy * quantity,
        )
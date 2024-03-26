package com.example.chooseu.core.diary.models

import com.example.chooseu.data.rest.api_service.dtos.foodItemDTO.Measure
import com.example.chooseu.data.rest.api_service.dtos.foodItemDTO.Nutrients

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

package com.example.chooseu.data.rest.api_service.dtos.nutrition_dto_extensions

import com.example.chooseu.data.rest.api_service.dtos.foodItemDTO.FoodHintDTO
import com.example.chooseu.data.rest.api_service.dtos.foodItemDTO.toFoodItem
import com.example.chooseu.ui.screens.nutrition_screen.FoodItem

/***
 * [toUniqueFoodItems] takes a list of FoodHintDTO and removes objects with the same FoodId
 */
fun List<FoodHintDTO>.toUniqueFoodItems(): List<FoodItem> {
    val uniqueFoodIds = this.map { it.food.foodId }.distinct()
    return uniqueFoodIds.mapNotNull { foodId ->
        val foodItem = this.firstOrNull() { it.food.foodId == foodId }?.toFoodItem()
        foodItem
    }
}
package com.example.chooseu.data.rest.api_service.dtos.foodItemDTO

import com.example.chooseu.ui.screens.nutrition_screen.FoodItem
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class FoodResponse(
    @SerialName("text") val text: String,
    @SerialName("hints") val hints: List<FoodHintDTO> = emptyList()
)

@Serializable
data class FoodHintDTO(
    @SerialName("food") val food: FoodItemDTO,
    @SerialName("measures") val measures: List<Measure>
)

@Serializable
data class FoodItemDTO(
    @SerialName("foodId") val foodId: String = "",
    @SerialName("label") val label: String = "",
    @SerialName("knownAs") val knownAs: String? = "",
    @SerialName("nutrients") val nutrients: Nutrients,
    @SerialName("category") val category: String = "",
    @SerialName("categoryLabel") val categoryLabel: String = "",
    @SerialName("image") val image: String = "",
    @SerialName("foodContentsLabel") val foodContentsLabel: String = ""
)


@Serializable
data class Nutrients(
    @SerialName("ENERC_KCAL") val energy: Double = 0.0,
    @SerialName("PROCNT") val protein: Double = 0.0,
    @SerialName("FAT") val fat: Double = 0.0,
    @SerialName("CHOCDF") val carbohydrates: Double = 0.0,
    @SerialName("FIBTG") val fiber: Double = 0.0
)

@Serializable
data class Measure(
    @SerialName("uri") val uri: String = "",
    @SerialName("label") val label: String = "",
    @SerialName("weight") val weight: Double = 0.0
)

fun List<FoodHintDTO>.toUniqueFoodItems(): List<FoodItem> {
    val uniqueFoodIds = this.map { it.food.foodId }.distinct()
    return uniqueFoodIds.mapNotNull { foodId ->
        val foodItem = this.firstOrNull() { it.food.foodId == foodId }?.toFoodItem()
        foodItem
    }
}

fun FoodHintDTO.toFoodItem(): FoodItem = FoodItem(
    quantity = 0,
    foodId = this.food.foodId,
    label = this.food.label,
    knownAs = this.food.knownAs,
    nutrients = this.food.nutrients,
    category = this.food.category,
    categoryLabel = this.food.categoryLabel,
    image = this.food.image,
    foodContentsLabel = this.food.foodContentsLabel,
    unitTypes = this.measures,
)



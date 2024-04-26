package com.example.chooseu.ui.screens.nutrition_screen

import NutrientDTO

data class NutritionDetail(
    val servingType: String,
    val quantifier: Double,
    private val nutritionValues: List<NutrientDTO> = emptyList(),
)
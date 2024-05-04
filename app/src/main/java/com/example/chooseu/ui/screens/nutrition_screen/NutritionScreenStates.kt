package com.example.chooseu.ui.screens.nutrition_screen

sealed class NutritionScreenStates {
    data class NutritionView(
        private val servingUris: List<String>,
        val foodId: String,
        val foodLabel: String,
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
        val errorMessage: String = "",
        val servings: List<String>,
        val selectedServing: String,
        val nutritionDetails: NutritionDetail?,
    ) : NutritionScreenStates()

    object Error : NutritionScreenStates()

    object NutritionStateSaved : NutritionScreenStates()
}
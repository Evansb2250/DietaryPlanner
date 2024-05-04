package com.example.chooseu.ui.screens.nutrition_screen

sealed class NutritionScreenStates {
    data class NutritionView(
        private val servingUris: List<String> = emptyList(),
        val foodId: String = "",
        val foodLabel: String = "",
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
        val errorMessage: String = "",
        val servings: List<String> = emptyList(),
        val selectedServing: String = "",
        val nutritionDetails: NutritionDetail? = null,
    ) : NutritionScreenStates(){
        fun getNutritionValueByLabel(nutritionLabel: String): Double{
           return  nutritionDetails?.nutritionStats?.firstOrNull{ it.label == nutritionLabel}?.quantity ?: 0.0
        }
    }

    object Error : NutritionScreenStates()

    object NutritionStateSaved : NutritionScreenStates()
}
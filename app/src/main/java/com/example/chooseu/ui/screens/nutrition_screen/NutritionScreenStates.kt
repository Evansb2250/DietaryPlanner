package com.example.chooseu.ui.screens.nutrition_screen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

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
    ) : NutritionScreenStates() {
        fun getNutritionValueByLabel(nutritionLabel: String): Double {
            return nutritionDetails?.nutritionStats?.firstOrNull { it.label == nutritionLabel }?.quantity
                ?: 0.0
        }

        fun getQuantityCount(): Double {
            return nutritionDetails?.quantifier?.toDouble() ?: 0.0
        }
    }

    data class Error(
        val errorMessage: String = ""
    ) : NutritionScreenStates() {
        val showDialog: MutableState<Boolean> = mutableStateOf(true)
    }

    object NutritionStateSaved : NutritionScreenStates()
}
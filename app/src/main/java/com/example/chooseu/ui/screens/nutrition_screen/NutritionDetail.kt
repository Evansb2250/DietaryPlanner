package com.example.chooseu.ui.screens.nutrition_screen

import NutrientDTO
import com.example.chooseu.utils.NumberUtils
import java.text.DecimalFormat

private val decimalFormat = DecimalFormat("#.##")
fun NutrientDTO.printNutritionValue(): String =
    "${decimalFormat.format(this.quantity)} ${this.unit ?: "g"}"

data class NutritionDetail(
    val servingType: String,
    val quantifier: String,
    private val nutritionValues: List<NutrientDTO> = emptyList(),
) {


    val nutritionStats: List<NutrientDTO> = nutritionValues.map {
        it.copy(
            quantity = it.quantity * NumberUtils.stringToDouble(quantifier),
            unit = it.unit ?: "g",
        )
    }

}


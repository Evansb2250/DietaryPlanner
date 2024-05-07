package com.example.chooseu.data.rest.api_service.dtos.nutrition_dto_extensions

import com.example.chooseu.ui.screens.nutrition_screen.NutritionDetail
import com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.*

fun TotalNutrientsDTO.toNutritionDetail(
    servingType: String,
    quantity: String = "1.0",
): NutritionDetail = NutritionDetail(
    servingType = servingType,
    quantifier = com.example.chooseu.utils.NumberUtils.updateStringToValidNumber(quantity),
    nutritionValues = listOf(
        this.enercKCAL.copy(
            label = NutritionConstants.CALORIES,
        ),
        this.procnt.copy(
            label = NutritionConstants.PROTEIN,
        ),
        this.fapu.copy(
            label = NutritionConstants.POLY_FAT,
        ),
        this.fams.copy(
            label = NutritionConstants.MONOUNSATURATED_FAT,
        ),
        this.fasat.copy(
            label = NutritionConstants.SATURATED_FAT
        ),
        this.fatrn.copy(
            label = NutritionConstants.TRANS_FAT,
        ),
        this.fat.copy(
            label = NutritionConstants.TOTAL_LIPID_FATS,
        ),
        this.fiber.copy(
            label = NutritionConstants.FIBER,
        ),
        this.sugar.copy(
            label = NutritionConstants.SUGAR,
        ),
        this.addedSugar.copy(
            label = NutritionConstants.ADDED_SUGAR,
        ),
        this.chocdfNet.copy(
            label = NutritionConstants.TOTAL_CARBS,
        ),
        this.cholesterol.copy(
            label = NutritionConstants.CHOLESTEROL,
        ),
        this.sodium.copy(
            label = NutritionConstants.SODIUM,
        ),
        this.vitaminC.copy(
            label = NutritionConstants.VITAMIN_C,
        ),
        this.iron.copy(
            label = NutritionConstants.IRON,
        ),
        this.vitaminD.copy(
            label = NutritionConstants.VITAMIN_D,
        ),
        this.potassium.copy(
            label = NutritionConstants.POTASSIUM,
        ),
        this.calcium.copy(
            label = NutritionConstants.CALCIUM,
        )
    ),
)
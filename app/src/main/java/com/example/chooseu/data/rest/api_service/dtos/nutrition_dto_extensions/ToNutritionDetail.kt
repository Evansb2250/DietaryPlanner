package com.example.chooseu.data.rest.api_service.dtos.nutrition_dto_extensions

import TotalNutrientsDTO
import com.example.chooseu.ui.screens.nutrition_screen.NutritionDetail

fun TotalNutrientsDTO.toNutritionDetail(
    servingType: String,
    quantity: String = "1.0",
): NutritionDetail = NutritionDetail(
    servingType = servingType,
    quantifier = com.example.chooseu.utils.NumberUtils.updateStringToValidNumber(quantity),
    nutritionValues = listOf(
        this.enercKCAL.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.CALORIES,
        ),
        this.fasat.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.SATURATED_FAT
        ),
        this.fatrn.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.TRANS_FAT,
        ),
        this.cholesterol.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.CHOLESTEROL,
        ),
        this.sodium.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.SODIUM,
        ),
        this.chocdfNet.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.TOTAL_CARBS,
        ),
        this.fiber.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.FIBER,
        ),
        this.sugar.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.SUGAR,
        ),
        this.addedSugar.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.ADDED_SUGAR,
        ),
        this.procnt.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.PROTEIN,
        ),
        this.vitaminC.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.VITAMIN_C,
        ),
        this.iron.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.IRON,
        ),
        this.vitaminD.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.VITAMIN_D,
        ),
        this.potassium.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.POTASSIUM,
        ),
        this.calcium.copy(
            label = com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants.CALCIUM,
        )
    ),
)
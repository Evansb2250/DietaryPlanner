package com.example.chooseu.repo.foodRepository

import com.example.chooseu.ui.screens.nutrition_screen.NutritionDetail
import com.example.chooseu.data.rest.api_service.dtos.foodItemDTO.FoodResponse
import com.example.chooseu.core.SavedNutritionValue
import com.example.chooseu.utils.AsyncResponse

interface FoodRepository {
        suspend fun makeNetworkRequest(food: String): AsyncResponse<FoodResponse?>

        suspend fun getNutritionDetails(foodId:String, measureUri: String) : AsyncResponse<NutritionDetail?>

        suspend fun saveFoodDetails(foodDetails : SavedNutritionValue): AsyncResponse<String>
}
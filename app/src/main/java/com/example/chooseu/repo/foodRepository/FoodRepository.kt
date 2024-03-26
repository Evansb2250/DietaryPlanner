package com.example.chooseu.repo.foodRepository

import com.example.chooseu.data.rest.api_service.dtos.foodItemDTO.FoodResponse
import com.example.chooseu.utils.AsyncResponse

interface FoodRepository {
        suspend fun makeNetworkRequest(food: String): AsyncResponse<FoodResponse?>
}
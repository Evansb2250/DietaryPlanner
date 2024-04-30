package com.example.chooseu.repo.foodRepository

import NutritionInfo
import com.example.chooseu.ui.screens.nutrition_screen.NutritionDetail
import com.example.chooseu.common.Constants
import com.example.chooseu.data.rest.api_service.NutritionBody
import com.example.chooseu.data.rest.api_service.EdamamFoodApiService
import com.example.chooseu.data.rest.api_service.Ingredient
import com.example.chooseu.data.rest.api_service.dtos.foodItemDTO.FoodResponse
import com.example.chooseu.utils.AsyncResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import toNutritionDetail


class FoodRepositoryImp(
    private val edamamFoodService: EdamamFoodApiService
) : FoodRepository {
    override suspend fun makeNetworkRequest(food: String): AsyncResponse<FoodResponse?> {
        return withContext(Dispatchers.IO) {
            try {
                val response = edamamFoodService.getFoods(
                    ingr = food,
                )
                AsyncResponse.Success(data = response)
            } catch (e: CancellationException) {
                //just means we cancelled the job therefore an error message shouldn't be shown
                return@withContext AsyncResponse.Success(data = null)
            } catch (e: Exception) {
                // Handle exceptions such as network errors
                return@withContext AsyncResponse.Failed(
                    data = null,
                    message = e.message ?: "Exception caught"
                )
            }
        }
    }

    override suspend fun getNutritionDetails(
        foodId: String,
        measureUri: String,
    ): AsyncResponse<NutritionDetail?> {
        return withContext(Dispatchers.IO) {
            fetchNutritionInfo(
                foodId,
                measureUri,
            )
        }
    }


    private suspend fun fetchNutritionInfo(
        foodId: String,
        measureUri: String,
    ): AsyncResponse<NutritionDetail?> {
        return try {
            AsyncResponse.Success(
                data = edamamFoodService.requestNutrients(
                    appId = Constants.EDAMAM_APPLICATION_ID,
                    appKey = Constants.EDAMAM_APPLICATION_KEY,
                    jsonBody = NutritionBody(
                        ingredients = listOf(
                            Ingredient(
                                quantity = 1,
                                measureURI = measureUri,
                                foodId = foodId
                            )
                        )
                    )
                ).totalNutrients
                    .toNutritionDetail(
                        quantity = "1.0",
                        servingType = measureUri,
                    ),
            )
        } catch (e: Exception) {
            AsyncResponse.Failed(
                data = null,
                message = "Couldn't complete API request"
            )
        }
    }
}
package com.example.chooseu.data.rest.api_service

import NutritionInfo
import com.example.chooseu.common.Constants
import com.example.chooseu.data.rest.api_service.dtos.foodItemDTO.FoodResponse
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface EdamamFoodApiService {
    @GET("parser")
    suspend fun getFoods(
        @Query("app_id") appId: String = Constants.EDAMAM_APPLICATION_ID,
        @Query("app_key") appKey: String = Constants.EDAMAM_APPLICATION_KEY,
        @Query("nutrition-type") nutritionType: String = "cooking",
        @Query("category") genericCategory: String = "generic-foods",
        @Query("category") fastFoodCategory: String = "fast-foods",
        @Query("ingr") ingr: String,
    ): FoodResponse

    @POST("nutrients")
    suspend fun requestNutrients(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Body jsonBody: NutritionBody,
    ): NutritionInfo
}

@Serializable
data class NutritionBody(val ingredients: List<Ingredient>)

@Serializable
data class Ingredient(
    @SerializedName("quantity") val quantity: Int,
    @SerializedName("measureURI") val measureURI: String,
    @SerializedName("foodId") val foodId: String
)

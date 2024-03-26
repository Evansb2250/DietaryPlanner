package com.example.chooseu.data.rest.api_service

import com.example.chooseu.common.Constants
import com.example.chooseu.data.rest.api_service.dtos.foodItemDTO.FoodResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EdamamFoodApiService {
    @GET("parser")
    fun getFoods(
        @Query("app_id") appId: String = Constants.EDAMAM_APPLICATION_ID,
        @Query("app_key") appKey: String = Constants.EDAMAM_APPLICATION_KEY,
        @Query("nutrition-type") nutritionType: String = "cooking",
        @Query("category") category: String = "generic-foods",
        @Query("ingr") ingr: String,
    ): Call<FoodResponse>
}
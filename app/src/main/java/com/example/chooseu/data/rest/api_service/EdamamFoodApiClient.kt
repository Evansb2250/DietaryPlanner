package com.example.chooseu.data.rest.api_service

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit


const val BASE_URL = "https://api.edamam.com/api/food-database/v2/"
object EdamamFoodApiClient {
    private val json = Json { ignoreUnknownKeys = true }
    private lateinit var instance: EdamamFoodApiService
        fun getService(): EdamamFoodApiService {
            if(!::instance.isInitialized){
                val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                    .build()

                instance = retrofit.create(EdamamFoodApiService::class.java)
            }

            return instance
        }
}
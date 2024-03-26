package com.example.chooseu.repo.foodRepository

import android.util.Log
import com.example.chooseu.data.rest.api_service.EdamamFoodApiService
import com.example.chooseu.data.rest.api_service.dtos.foodItemDTO.FoodResponse
import com.example.chooseu.utils.AsyncResponse
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume


class FoodRepositoryImp(
    private val edamamFoodService: EdamamFoodApiService
) : FoodRepository {
    override suspend fun makeNetworkRequest(food: String): AsyncResponse<FoodResponse?> {
        try {
            return withContext(Dispatchers.IO) {
                suspendCancellableCoroutine {
                    edamamFoodService.getFoods(
                        ingr = food,
                    ).enqueue(
                        object : Callback<FoodResponse?> {
                            override fun onResponse(
                                call: Call<FoodResponse?>,
                                response: Response<FoodResponse?>
                            ) {
                                it.resume(
                                    AsyncResponse.Success(data = response.body())
                                )
                            }

                            override fun onFailure(call: Call<FoodResponse?>, t: Throwable) {
                                Log.d("APIRQUEST", "failed ${t.message}")
                                it.resume(
                                    AsyncResponse.Failed(data = null, message = t.message ?: "")
                                )
                            }
                        }
                    )

                }
            }
        } catch (e: CancellationException) {
            //just means we cancelled the job therefore an error message shouldn't be shown
            return AsyncResponse.Success(data = null)
        } catch (e: Exception) {
            // Handle exceptions such as network errors
            return AsyncResponse.Failed(data = null, message = e.message ?: "Exception caught")
        }
    }
}
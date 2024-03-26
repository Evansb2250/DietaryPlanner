package com.example.chooseu.core.diary.nutrition


import com.example.chooseu.core.diary.cache.FoodItemCache
import javax.inject.Inject
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlin.properties.Delegates

@HiltViewModel
class NutritionViewModel @Inject constructor(
    val cache: FoodItemCache,
) : ViewModel() {
    var state by mutableStateOf("")
        private set


    var date by Delegates.notNull<Long>()



    fun loadData(foodId: String) {
        state = cache.map[foodId]?.foodId ?: "no data"
    }
}
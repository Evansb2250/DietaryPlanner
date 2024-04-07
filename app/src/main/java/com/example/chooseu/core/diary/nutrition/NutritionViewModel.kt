package com.example.chooseu.core.diary.nutrition


import com.example.chooseu.core.diary.cache.FoodItemCache
import javax.inject.Inject
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.repo.foodRepository.FoodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val foodRepository: FoodRepository,
    val cache: FoodItemCache,
) : ViewModel() {
    var state by mutableStateOf("")
        private set


    var date by Delegates.notNull<Long>()


    fun loadData(foodId: String) {
        viewModelScope.launch {
            val foodItem = cache.map[foodId] ?: return@launch
            foodRepository.getNutritionDetails(
                foodId = foodItem.foodId,
                measureUri = foodItem.unitTypes.first().uri
            )

        }
    }
}
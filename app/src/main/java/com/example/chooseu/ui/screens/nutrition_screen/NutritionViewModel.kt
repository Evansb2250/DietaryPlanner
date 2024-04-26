package com.example.chooseu.ui.screens.nutrition_screen


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.core.cache.FoodItemCache
import com.example.chooseu.di.VMAssistFactoryModule
import com.example.chooseu.repo.foodRepository.FoodRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch

@HiltViewModel(
    assistedFactory = VMAssistFactoryModule.NutritionViewModelFactory::class
)
class NutritionViewModel @AssistedInject constructor(
    private val foodRepository: FoodRepository,
    private val cache: FoodItemCache,
    @Assisted private val day: Long?,
    @Assisted("user") private val userId: String?,
    @Assisted("food") private val foodId: String?,
) : ViewModel() {
    var state by mutableStateOf("")
        private set

    val servingOptions by mutableStateOf(cache.map[foodId]?.unitTypes?.drop(1) ?: emptyList())


    var selectedServing by mutableStateOf(servingOptions.firstOrNull()?.label ?: "")
        private set

    fun updateSelectedServing(serving: String) {
        selectedServing = serving
        loadData()
    }

    var nutritionalValues: NutritionDetail by mutableStateOf(
        NutritionDetail(
            servingType = selectedServing,
            quantifier = 0.0,
        )
    )


    fun loadData() {
        viewModelScope.launch {
            nutritionalValues = foodRepository.getNutritionDetails(
                foodId = foodId ?: "",
                measureUri = servingOptions.first { it.label.equals(selectedServing) }.uri
            )

        }
    }
}
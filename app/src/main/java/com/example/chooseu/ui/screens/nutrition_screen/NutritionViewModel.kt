package com.example.chooseu.ui.screens.nutrition_screen


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.core.cache.FoodItemCache
import com.example.chooseu.di.VMAssistFactoryModule
import com.example.chooseu.repo.foodRepository.FoodRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class NutritionScreenStates {
    data class NutritionView(
        private val servingUris: List<String>,
        val foodId: String,
        val foodLabel: String,
        val isLoading: Boolean = false,
        val hasError: Boolean = false,
        val errorMessage: String? = null,
        val servings: List<String>,
        val selectedServing: String,
        val nutritionDetails: NutritionDetail?,
    ) : NutritionScreenStates()
}

@HiltViewModel(
    assistedFactory = VMAssistFactoryModule.NutritionViewModelFactory::class
)
class NutritionViewModel @AssistedInject constructor(
    private val foodRepository: FoodRepository,
    @Assisted private val day: Long?,
    @Assisted("user") private val userId: String?,
    @Assisted("food") private val foodId: String?,
    cache: FoodItemCache,
) : ViewModel() {
    var _state: MutableStateFlow<NutritionScreenStates.NutritionView> = MutableStateFlow(
        NutritionScreenStates.NutritionView(
            servingUris = emptyList(),
            foodId = "",
            foodLabel = "",
            isLoading = true,
            servings = emptyList(),
            selectedServing = "",
            nutritionDetails = null
        )
    )
    val state = _state.asStateFlow()

    private val servingOptions by mutableStateOf(
        cache.map[foodId]?.unitTypes?.drop(1) ?: emptyList()
    )

    fun updateNutritionServing(servingUpdate: String) {
        loadData(servingUpdate)
    }

    fun updateNutritionQuantity(newState: NutritionScreenStates.NutritionView) {
        _state.value = newState
    }

    fun loadData(
        selectedServingSize: String = servingOptions.firstOrNull()?.label ?: ""
    ) {
        setLoadingState()

        viewModelScope.launch {
            if (foodId != null) {
                _state.value = foodRepository.getNutritionDetails(
                    foodId = foodId,
                    measureUri = servingOptions.first { it.label.equals(selectedServingSize) }.uri
                ).let {
                    NutritionScreenStates.NutritionView(
                        foodId = foodId,
                        foodLabel = "",
                        servings = servingOptions.map { it.label },
                        servingUris = servingOptions.map { it.uri },
                        selectedServing = selectedServingSize,
                        nutritionDetails = it
                    )
                }
            }
        }
    }

    private fun setLoadingState() {
        _state.value = _state.value.copy(
            isLoading = true
        )
    }
}
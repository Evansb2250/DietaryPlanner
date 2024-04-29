package com.example.chooseu.ui.screens.nutrition_screen


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.core.cache.FoodItemCache
import com.example.chooseu.di.VMAssistFactoryModule
import com.example.chooseu.repo.foodRepository.FoodRepository
import com.example.chooseu.utils.NumberUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class NutritionScreenStates {
    object  Loading : NutritionScreenStates()
    data class NutritionView(
        val foodId: String,
        val foodLabel: String,
        val servings: List<String>,
        val selectedServing: String,
        val nutritionDetails: NutritionDetail,
    ): NutritionScreenStates()
}

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
    var _state: MutableStateFlow<NutritionScreenStates> = MutableStateFlow(
        NutritionScreenStates.Loading
    )
    val state = _state.asStateFlow()

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
            quantifier = "0",
        )
    )

    fun updateQuantity(quantity: String) {
        nutritionalValues = nutritionalValues.copy(
            quantifier = NumberUtils.updateStringToValidNumber(quantity),
        )
    }

    fun loadData() {
        viewModelScope.launch {
            nutritionalValues = foodRepository.getNutritionDetails(
                foodId = foodId ?: "",
                measureUri = servingOptions.first { it.label.equals(selectedServing) }.uri
            )

        }
    }
}
package com.example.chooseu.ui.screens.nutrition_screen


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.core.cache.FoodItemCache
import com.example.chooseu.di.VMAssistFactoryModule
import com.example.chooseu.navigation.components.navmanagers.MainFlowNavManager
import com.example.chooseu.repo.foodRepository.FoodRepository
import com.example.chooseu.utils.AsyncResponse
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SavedNutritionValue(
    val userId: String,
    val day: Long,
    val foodId: String,
    val foodName: String,
    val foodServingUri: String,
    val quantity: Double,
    val protein: Double,
    val carbs: Double,
    val totalCalories: Double,
)

@HiltViewModel(
    assistedFactory = VMAssistFactoryModule.NutritionViewModelFactory::class
)
class NutritionViewModel @AssistedInject constructor(
    private val foodRepository: FoodRepository,
    private val navManager: MainFlowNavManager,
    @Assisted private val day: Long?,
    @Assisted("user") private val userId: String?,
    @Assisted("food") private val foodId: String?,
    cache: FoodItemCache,
) : ViewModel() {
    lateinit var _state: MutableStateFlow<NutritionScreenStates>

    val state = _state.asStateFlow()

    init {
        _state.value = if (day != null && userId != null && foodId != null)
            NutritionScreenStates.NutritionView(
                servingUris = emptyList(),
                foodId = "",
                foodLabel = "",
                isLoading = true,
                servings = emptyList(),
                selectedServing = "",
                nutritionDetails = null
            )
        else
            NutritionScreenStates.Error
    }


    private val foodLabel = cache.map[foodId]?.label ?: "Unkown"

    private val servingOptions by mutableStateOf(
        cache.map[foodId]?.unitTypes?.drop(1) ?: emptyList()
    )

    fun updateNutritionServing(servingUpdate: String) {
        loadData(servingUpdate)
    }

    fun updateNutritionQuantity(newState: NutritionScreenStates.NutritionView) {
        _state.value = newState
    }

    fun onBackPress() {
        navManager.onBackPress()
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
                ).let { response ->
                    handleNetworkRequest(
                        foodId,
                        selectedServingSize,
                        response,
                    )
                }
            }
        }
    }


    private fun handleNetworkRequest(
        foodId: String,
        selectedServingSize: String,
        response: AsyncResponse<NutritionDetail?>
    ): NutritionScreenStates.NutritionView {
        return when (response) {
            is AsyncResponse.Failed ->
                NutritionScreenStates.NutritionView(
                    foodId = foodId,
                    foodLabel = foodLabel,
                    isLoading = false,
                    hasError = true,
                    errorMessage = response.message ?: "network error",
                    servings = servingOptions.map { it.label },
                    servingUris = servingOptions.map { it.uri },
                    selectedServing = selectedServingSize,
                    nutritionDetails = null,
                )

            is AsyncResponse.Success -> {
                NutritionScreenStates.NutritionView(
                    foodId = foodId,
                    foodLabel = foodLabel,
                    servings = servingOptions.map { it.label },
                    servingUris = servingOptions.map { it.uri },
                    selectedServing = selectedServingSize,
                    nutritionDetails = response.data
                )
            }
        }
    }

    private fun setLoadingState() {
        val nutritionState = state.value
        if (nutritionState is NutritionScreenStates.NutritionView) {
            _state.value = nutritionState.copy(
                isLoading = true
            )
        }
    }
}
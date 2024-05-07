package com.example.chooseu.ui.screens.nutrition_screen


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.core.cache.FoodItemCache
import com.example.chooseu.data.rest.api_service.dtos.nutritionInfo.NutritionConstants
import com.example.chooseu.di.VMAssistFactoryModule
import com.example.chooseu.core.SavedNutritionValue
import com.example.chooseu.navigation.components.navmanagers.MainFlowNavManager
import com.example.chooseu.repo.foodRepository.FoodRepository
import com.example.chooseu.utils.AsyncResponse
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
    private lateinit var _state: MutableStateFlow<NutritionScreenStates>

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


    private val foodLabel = cache.map[foodId]?.label ?: "Unknown"

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

    fun add(nutritionView: NutritionScreenStates.NutritionView) = viewModelScope.launch {
        try {
            foodRepository.saveFoodDetails(
                SavedNutritionValue(
                    userId = userId!!,
                    day = day!!,
                    foodId = nutritionView.foodId,
                    foodName = nutritionView.foodLabel,
                    foodServingUri = getServingUri(nutritionView.selectedServing),
                    servingLabel = nutritionView.selectedServing,
                    quantity = nutritionView.getQuantityCount(),
                    protein = nutritionView.getNutritionValueByLabel(NutritionConstants.PROTEIN),
                    carbs = nutritionView.getNutritionValueByLabel(NutritionConstants.TOTAL_CARBS),
                    totalCalories = nutritionView.getNutritionValueByLabel(NutritionConstants.CALORIES),
                )
            )

        } catch (
            e: Exception
        ) {

        }
    }

    fun loadData(
        selectedServingSize: String = servingOptions.firstOrNull()?.label ?: ""
    ) {
        setLoadingState()

        viewModelScope.launch {
            if (foodId != null) {
                _state.value = foodRepository.getNutritionDetails(
                    foodId = foodId,
                    measureUri = getServingUri(selectedServingSize),
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

    private fun getServingUri(
        selectedServingSize: String,
    ): String {
        return servingOptions.first { it.label.equals(selectedServingSize) }.uri
    }

    private fun setLoadingState() {
        val nutritionState = state.value
        if (nutritionState is NutritionScreenStates.NutritionView) {
            _state.value = nutritionState.copy(
                isLoading = true
            )
        }
    }

    private fun setErrorState(
        errorMessage: String,
    ) {
        _state.value = NutritionScreenStates.NutritionView(
            hasError = true,
            errorMessage = errorMessage,
        )
    }
}
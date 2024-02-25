package com.example.chooseu.core.diary

import androidx.lifecycle.ViewModel
import com.example.chooseu.core.diary.states.DiaryScreenStates
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.navmanagers.AppNavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DiaryScreenViewModel @Inject constructor(
    val navigationManager: AppNavManager,
) : ViewModel() {
    private val _state: MutableStateFlow<DiaryScreenStates> = MutableStateFlow(
        DiaryScreenStates.MealDiary(date = LocalDate.now())
    )

    val state = _state.asStateFlow()

    fun addFoodItem(
        day: String,
        foodType: String,
    ) {
        navigationManager.navigate(
            GeneralDestinations.FoodSearchDestination,
            arguments = mapOf(
                "day" to day,
                "foodType" to foodType,
            )
        )
    }
}
package com.example.googlelightcalendar.core.diary

import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.core.diary.states.DiaryScreenStates
import com.example.googlelightcalendar.navigation.components.destinations.GeneralDestinations
import com.example.googlelightcalendar.navigation.components.navmanagers.BottomNavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DiaryScreenViewModel @Inject constructor(
    val navigationManager: BottomNavManager,
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
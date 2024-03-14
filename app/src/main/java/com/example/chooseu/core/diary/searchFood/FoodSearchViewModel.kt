package com.example.chooseu.core.diary.searchFood

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.example.chooseu.navigation.components.destinations.BottomNavBarDestinations
import com.example.chooseu.navigation.components.navmanagers.AppNavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.chooseu.navigation.components.destinations.destinationArguments.DiaryArgs

@HiltViewModel
class FoodSearchViewModel @Inject constructor(val navManager: AppNavManager) : ViewModel() {

     var date: Long = 0L
        private set

    var text by mutableStateOf("")

    fun navigateBackToFoodDiary() {
        navManager.navigate(
            BottomNavBarDestinations.Diary,
            mapOf(
                DiaryArgs.LONG_DATE.name to "$date"
            )
        )
    }

    fun setLongDate(date:Long){
        this.date = date
    }

    fun searchFoodItem(foodName: String) {
        text = ""
    }

    private fun clearText() {
        text = ""
    }
}
package com.example.chooseu.core.diary.searchFood

import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import com.example.chooseu.navigation.components.destinations.BottomNavBarDestinations
import com.example.chooseu.navigation.components.navmanagers.AppNavManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

@HiltViewModel
class FoodSearchViewModel @Inject constructor(val navManager: AppNavManager,): ViewModel() {

    var text by  mutableStateOf("")

    fun navigateBackToFoodDiary(){
       navManager.navigate(BottomNavBarDestinations.Diary)
    }

    fun searchFoodItem(foodName: String,){
        text = ""
    }

    private fun clearText(){
        text = ""
    }
}
package com.example.chooseu.ui.screens.food_search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chooseu.core.diary.searchFood.FoodSearchViewModel

@Composable
fun FoodSearchScreen(
    title: String,
    dateLong: Long,
    vm: FoodSearchViewModel = hiltViewModel()
) {
    LaunchedEffect(
        key1 = dateLong,
    ) {
        vm.setLongDate(dateLong)
    }

    FoodSearchScreenContent(
        title = title,
        state = vm.state.collectAsState().value,
        navigateBackToFoodDiary = vm::navigateBackToFoodDiary,
        searchFoodItem = vm::updateSearchText,
        clearDialog = vm::reset,
        addItem = vm::updateFoodItemList,
        viewNutrientDetails = vm::viewNutrientDetails
    )
}
package com.example.chooseu.ui.screens.nutrition_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chooseu.di.VMAssistFactoryModule


@Composable
fun NutritionScreen(
    userId: String?,
    foodId: String?,
    dayLong: Long?,
    vm: NutritionViewModel = hiltViewModel(
        creationCallback = { factory: VMAssistFactoryModule.NutritionViewModelFactory ->
            factory.create(
                day = dayLong,
                userId = userId,
                foodId = foodId,
            )
        }
    ),
) {
    LaunchedEffect(key1 = foodId) {
        vm.loadData()
    }


    NutritionScreenContent(
        state = vm.state.collectAsStateWithLifecycle().value,
        updateNutritionScreen = vm::updateNutritionQuantity,
        updateNutritionServing = vm::updateNutritionServing,
    )
}
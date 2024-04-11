package com.example.chooseu.ui.screens.nutrition_screen

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chooseu.core.diary.nutrition.NutritionViewModel


@Composable
fun NutritionScreen(
   userId: String = "",
   foodId: String,
   vm: NutritionViewModel = hiltViewModel()
){
   LaunchedEffect(key1 = foodId){
      vm.loadData(foodId)
   }

   Text(
      text =  vm.state
   )





}
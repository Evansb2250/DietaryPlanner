package com.example.chooseu.ui.screens.nutrition_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chooseu.common.sidePadding
import com.example.chooseu.di.VMAssistFactoryModule
import com.example.chooseu.ui.ui_components.menu.CustomDropDownMenu
import com.example.chooseu.ui.ui_components.text_fields.NumberTextField


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

    Column(
        modifier = Modifier
            .padding(
                horizontal = sidePadding
            )
            .fillMaxSize(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier.weight(.5f),
                text = "Serving Type:",
                color = Color.White,
                textAlign = TextAlign.Left,
            )

            CustomDropDownMenu(
                modifier = Modifier.weight(.5f),
                selectedOptionText = vm.selectedServing,
                options = vm.servingOptions.map { it.label },
                enable = true,
                onOptionChange = { it ->
                    vm.updateSelectedServing(it)
                },
                onIndexChange = {},
            )
        }
        Spacer(modifier = Modifier.size(20.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                modifier = Modifier.weight(.5f),
                text = "Quantity:",
                color = Color.White,
                textAlign = TextAlign.Left,
            )

            NumberTextField(
                modifier = Modifier.weight(.5f),
                value = vm.nutritionalValues.quantifier,
                onValueChange = vm::updateQuantity,
            )
        }

        LazyColumn() {
            items(vm.nutritionalValues.nutritionStats) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Text(
                        modifier = Modifier.weight(.5f),
                        text = "${it.label}:",
                        color = Color.White,
                        textAlign = TextAlign.Left,
                    )


                    Text(
                        modifier = Modifier.weight(.5f),
                        text = it.printNutritionValue(),
                        color = Color.White,
                        textAlign = TextAlign.Right,
                    )
                }
            }
        }
    }
}
package com.example.chooseu.ui.screens.nutrition_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chooseu.common.sidePadding
import com.example.chooseu.ui.ui_components.dialog.LoadingDialog
import com.example.chooseu.ui.ui_components.menu.CustomDropDownMenu
import com.example.chooseu.ui.ui_components.text_fields.NumberTextField

@Composable
fun NutritionScreenContent(
    state: NutritionScreenStates,
    updateNutritionScreen: (NutritionScreenStates.NutritionView) -> Unit = {},
    updateNutritionServing: (String) -> Unit = {},
) {
    when (state) {
        is NutritionScreenStates.NutritionView -> {

            Column(
                modifier = Modifier
                    .padding(
                        horizontal = sidePadding
                    )
                    .fillMaxSize(),
            ) {

                if (state.isLoading) {
                    Box() {
                        LoadingDialog()
                    }
                }
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
                        selectedOptionText = state.selectedServing,
                        options = state.servings,
                        enable = true,
                        onOptionChange = { serving ->
                            updateNutritionServing(serving)
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
                        value = state.nutritionDetails?.quantifier ?: "",
                        onValueChange = {
                            updateNutritionScreen(
                                state.copy(
                                    nutritionDetails = state.nutritionDetails?.copy(
                                        quantifier = it
                                    )
                                )
                            )
                        }
                    )
                }

                LazyColumn() {
                    items(state.nutritionDetails?.nutritionStats ?: emptyList()) {
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
    }
}
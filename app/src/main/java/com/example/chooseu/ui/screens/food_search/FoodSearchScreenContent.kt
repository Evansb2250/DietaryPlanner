package com.example.chooseu.ui.screens.food_search

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.chooseu.R
import com.example.chooseu.common.sidePadding
import com.example.chooseu.ui.screens.food_search.states.FoodItemListActions
import com.example.chooseu.ui.screens.food_search.states.FoodSearchStates
import com.example.chooseu.core.toolbar_states.ToolBarState
import com.example.chooseu.ui.screens.nutrition_screen.FoodItem
import com.example.chooseu.ui.screens.nutrition_screen.partitionFoodItemsRecommendations
import com.example.chooseu.ui.ui_components.custom_column.AppColumnContainer
import com.example.chooseu.ui.ui_components.dialog.ErrorDialog
import com.example.chooseu.ui.ui_components.dialog.LoadingDialog
import com.example.chooseu.ui.ui_components.toolbar.ChooseUToolBar

@Composable
fun FoodSearchScreenContent(
    title: String,
    state: FoodSearchStates.LoggingFoodItem,
    navigateBackToFoodDiary: () -> Unit = {},
    searchFoodItem: (String) -> Unit = {},
    clearDialog: () -> Unit = {},
    viewNutrientDetails: (foodId: String) -> Unit = {},
    addItem: (foodItem: FoodItem) -> Unit = {}
) {
    Scaffold(
        topBar = {
            ChooseUToolBar(
                toolBarState = ToolBarState.Navigated(
                    title = title,
                ),
                navigateBack = navigateBackToFoodDiary,
                navigateToActionDestination = {}
            )
        }
    ) { it ->
        AppColumnContainer(
        ) {
            Box {
                if (state.loading) {
                    LoadingDialog()
                }

                if (state.errorState.isError) {
                    Log.d("ErrorsFound", " this error ${state.errorState.message}")
                    ErrorDialog(
                        title = "Found Error",
                        error = state.errorState.message,
                        onDismiss = clearDialog
                    )
                }

                Column {
                    OutlinedTextField(
                        modifier = Modifier
                            .padding(it)
                            .fillMaxWidth(),
                        value = state.userInput,
                        onValueChange = searchFoodItem,
                        singleLine = true,
                        leadingIcon = {
                            Image(
                                modifier = Modifier
                                    .padding(horizontal = sidePadding),
                                painter = painterResource(
                                    id = R.drawable.search_icon,
                                ),
                                contentDescription = "",
                            )
                        },
                        shape = CircleShape,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                        )
                    )

                    LazyColumn(
                        modifier = Modifier.padding(it),
                        verticalArrangement = Arrangement.Top,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val (bestMatched, leastMatched) = state.foodItemsFound.value.partitionFoodItemsRecommendations()

                        item {
                            if (state.foodItemsFound.value.isNotEmpty()) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Best Matched",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Left,
                                    fontSize = TextUnit(18f, TextUnitType.Sp)
                                )
                            }
                        }

                       //Items in the top 3 best matched
                        items(
                            items = bestMatched
                        ) { foodItem ->
                            FoodCard(
                                foodItem = foodItem,
                                addItem = addItem,
                                viewNutrientDetails = viewNutrientDetails
                            )
                            Spacer(
                                modifier = Modifier.size(10.dp),
                            )
                        }

                        item {
                            if (state.foodItemsFound.value.size > 3) {
                                Text(
                                    modifier = Modifier.fillMaxWidth(),
                                    text = "Other Recommendations",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    textAlign = TextAlign.Left,
                                    fontSize = TextUnit(18f, TextUnitType.Sp)
                                )
                            }
                        }

                        //All other items
                        items(
                            items = leastMatched
                        ) { foodItem ->
                            FoodCard(
                                foodItem = foodItem,
                                addItem = addItem,
                                viewNutrientDetails = viewNutrientDetails
                            )
                            Spacer(
                                modifier = Modifier.size(10.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
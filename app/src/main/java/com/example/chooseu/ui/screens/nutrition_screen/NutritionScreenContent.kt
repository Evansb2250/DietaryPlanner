package com.example.chooseu.ui.screens.nutrition_screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chooseu.R
import com.example.chooseu.common.sidePadding
import com.example.chooseu.ui.theme.appColor
import com.example.chooseu.ui.theme.yellowMain
import com.example.chooseu.ui.ui_components.custom_column.AppColumnContainer
import com.example.chooseu.ui.ui_components.dialog.ErrorDialog
import com.example.chooseu.ui.ui_components.dialog.LoadingDialog
import com.example.chooseu.ui.ui_components.menu.CustomDropDownMenu
import com.example.chooseu.ui.ui_components.text_fields.NumberTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionScreenContent(
    state: NutritionScreenStates,
    updateNutritionScreen: (NutritionScreenStates.NutritionView) -> Unit = {},
    updateNutritionServing: (String) -> Unit = {},
    dismissErrorDialog: () -> Unit = {},
    onBackNavigation: () -> Unit = {},
) {
    when (state) {
        is NutritionScreenStates.NutritionView -> {
            Scaffold(
                containerColor = appColor,
                topBar = {
                    TopAppBar(
                        modifier = Modifier.padding(
                            horizontal = sidePadding,
                        ),
                        navigationIcon = {
                            Image(
                                modifier = Modifier.clickable {
                                    onBackNavigation()
                                },
                                painter = painterResource(id = R.drawable.back_arrow),
                                contentDescription = " ",
                            )
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = appColor,
                        ),
                        title = {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                color = Color.White,
                                text = state.foodLabel,
                                textAlign = TextAlign.Center,
                            )
                        },
                        actions = {
                            OutlinedButton(
                                onClick = {},
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(
                                    width = 1.dp,
                                    color = yellowMain,
                                ),
                            ) {
                                Text(text = "Add")
                            }
                        }
                    )
                }
            ) { padding ->

                HorizontalDivider(
                    modifier = Modifier.padding(
                        padding,
                    ),
                    thickness = 0.5.dp,
                    color = Color.White,
                )
                Column(
                    modifier = Modifier
                        .padding(
                            padding
                        )
                        .fillMaxSize(),
                ) {
                    Column(
                        modifier = Modifier
                            .padding(
                                top = 18.dp,
                                start = sidePadding,
                                end = sidePadding,
                            )
                            .fillMaxSize(),
                    ) {
                        if (state.isLoading) {
                            Box() {
                                LoadingDialog()
                            }
                        }

                        if (state.hasError) {
                            Box {
                                ErrorDialog(
                                    title = "Error found",
                                    error = state.errorMessage,
                                    onDismiss = dismissErrorDialog,
                                )
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
                                    modifier = Modifier
                                        .padding(
                                            vertical = 4.dp
                                        )
                                        .fillMaxWidth(),
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
                                HorizontalDivider(
                                    color = Color.White,
                                )
                            }
                        }
                    }
                }
            }
        }

        NutritionScreenStates.Error -> {
            AppColumnContainer {
                Image(
                    painter = painterResource(
                        id = R.drawable.error_exclamation_icon
                    ),
                    contentDescription = "error image",
                )
            }
        }

        NutritionScreenStates.NutritionStateSaved -> {
            //TODO
        }
    }
}
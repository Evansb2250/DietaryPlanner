package com.example.chooseu.screens.register.physical

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.chooseu.R
import com.example.chooseu.core.registration.Genders
import com.example.chooseu.core.registration.state.PhysicalDetailState
import com.example.chooseu.core.registration.state.heightUnits
import com.example.chooseu.core.registration.state.weightUnits
import com.example.chooseu.screens.register.previews.PhysicalDetailPreview
import com.example.chooseu.ui.ui_components.buttons.StandardButton
import com.example.chooseu.ui.ui_components.calendar.DateSelector
import com.example.chooseu.ui.ui_components.custom_column.AppColumnContainer
import com.example.chooseu.ui.ui_components.dialog.ErrorDialog
import com.example.chooseu.ui.ui_components.dialog.GenericAlertDialog
import com.example.chooseu.ui.ui_components.dialog.LoadingDialog
import com.example.chooseu.ui.ui_components.menu.CustomDropDownMenu
import com.example.chooseu.ui.ui_components.text_fields.CustomOutlineTextField


@Composable
@Preview
fun PhysicalDetailContent(
    @PreviewParameter(PhysicalDetailPreview::class)
    state: PhysicalDetailState,
    createAccount: (PhysicalDetailState.PhysicalDetails) -> Unit = {},
    retry: () -> Unit = {},
) {

    AppColumnContainer(
        modifier = Modifier
            .fillMaxSize(),
        disableBackPress = false,
    ) {

        when (state) {
            PhysicalDetailState.Loading -> {
                AppColumnContainer(
                    modifier = Modifier
                        .fillMaxSize(),
                    disableBackPress = false,
                ) {
                    LoadingDialog()
                }
            }
            is PhysicalDetailState.PhysicalDetails -> {
                PhysicalDetailFormScreen(
                    state,
                    createAccount = createAccount,
                    retry = retry,
                )
            }

            PhysicalDetailState.Success ->  {
                AppColumnContainer(
                    modifier = Modifier
                        .fillMaxSize(),
                    disableBackPress = false,
                ) {
                    GenericAlertDialog(
                        title = {Text("Congratulations")}
                    ) {

                    }
                }
            }
        }


    }
}



@Composable
private fun PhysicalDetailFormScreen(
    state: PhysicalDetailState.PhysicalDetails,
    createAccount: (PhysicalDetailState.PhysicalDetails) -> Unit = {},
    retry: () -> Unit = {},
) {
    AppColumnContainer(
        modifier = Modifier
            .fillMaxSize(),
        disableBackPress = false,
    ) {
        if (
            state.errorState.isError
        ) {
            ErrorDialog(
                title = "Error",
                error = state.errorState.message ?: "Error detected",
                onDismiss = retry
            )
        }
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .align(
                    alignment = Alignment.CenterHorizontally,
                ),
            painter = painterResource(
                id = R.drawable.main_logo,
            ),
            contentDescription = "Logo"
        )

        Spacer(modifier = Modifier.size(30.dp))

        DateSelector(
            initialDate = state.birthDate,
            modifier = Modifier.fillMaxWidth(),
            onDateChange = { state.birthDate.value = it }
        )

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "Date of Birth - MM/DD/YYYY",
            textAlign = TextAlign.Start,
            color = Color.White,
            fontSize = TextUnit(
                value = 12f,
                type = TextUnitType.Sp
            )
        )

        Spacer(
            modifier = Modifier.size(30.dp),
        )

        Row(
            modifier = Modifier.padding(top = 8.dp)
        ) {

            CustomOutlineTextField(
                modifier = Modifier
                    .weight(5f),
                value = state.userWeight.value.weight,
                onValueChange = { weight ->
                    state.updateWeight(
                        weight
                    )
                },
                label = "weight",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            )

            CustomDropDownMenu(
                selectedOptionText = state.userWeight.value.weightType.type,
                modifier = Modifier
                    .padding(
                        horizontal = 10.dp,
                    )
                    .size(
                        width = 63.dp,
                        height = 56.dp,
                    )
                    .weight(2f),
                options = weightUnits.map { it.type },
                onOptionChange = { weightUnit: String ->
                    state.updateWeightMetrics(
                        weightUnit
                    )
                }
            )
        }

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Weight -> kg, lb",
            textAlign = TextAlign.Start,
            color = Color.White,
            fontSize = TextUnit(
                value = 12f,
                type = TextUnitType.Sp
            )
        )

        Spacer(modifier = Modifier.size(30.dp))

        Row(
            modifier = Modifier.padding(top = 8.dp)
        ) {

            CustomOutlineTextField(
                modifier = Modifier.weight(5f),
                value = state.userHeight.value.height,
                onValueChange = { height ->
                    state.updateHeight(height)
                },
                label = "Height",
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            )

            CustomDropDownMenu(
                selectedOptionText = state.userHeight.value.heightType.type,
                modifier = Modifier
                    .padding(
                        horizontal = 10.dp,
                    )
                    .size(
                        width = 63.dp,
                        height = 56.dp,
                    )
                    .weight(2f),
                options = heightUnits.map { it.type },
                onOptionChange = { unit ->
                    state.updateHeightMetrics(
                        unit
                    )
                }
            )
        }
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Height -> ft, in",
            textAlign = TextAlign.Start,
            color = Color.White,
            fontSize = TextUnit(
                value = 12f,
                type = TextUnitType.Sp
            )
        )

        Spacer(modifier = Modifier.size(30.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "Gender",
            color = Color.White,
            textAlign = TextAlign.Start,
        )
        Row(
            horizontalArrangement = Arrangement.Start,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = state.selectedGender.value == Genders.Male,
                    onClick = {
                        state.selectedGender.value = Genders.Male
                    },
                )
                Text(
                    text = Genders.Male.gender,
                    color = Color.White
                )
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = state.selectedGender.value == Genders.FEMALE,
                    onClick = {
                        state.selectedGender.value = Genders.FEMALE
                    },
                )
                Text(
                    text = Genders.FEMALE.gender,
                    color = Color.White,
                )
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = state.selectedGender.value == Genders.OTHER,
                    onClick = {
                        state.selectedGender.value = Genders.OTHER
                    },
                )
                Text(
                    text = Genders.OTHER.gender,
                    color = Color.White,
                )
            }
        }

        Spacer(
            modifier = Modifier.size(30.dp),
        )

        StandardButton(
            modifier = Modifier
                .align(
                    Alignment.End
                )
                .fillMaxWidth(),
            text = "Create Account",
            onClick = {
                createAccount(state)
            },
        )
    }
}
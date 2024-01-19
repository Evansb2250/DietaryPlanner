package com.example.googlelightcalendar.screens.register

import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.collectAsState
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.core.registration.Genders
import com.example.googlelightcalendar.core.registration.PhysicalDetailsViewModel
import com.example.googlelightcalendar.core.registration.state.HeightUnits
import com.example.googlelightcalendar.core.registration.state.PhysicalDetailState
import com.example.googlelightcalendar.core.registration.state.UnitsOfWeight
import com.example.googlelightcalendar.core.registration.state.heightUnits
import com.example.googlelightcalendar.core.registration.state.weightUnits
import com.example.googlelightcalendar.screens.register.previews.PhysicalDetailPreview
import com.example.googlelightcalendar.ui_components.buttons.StandardButton
import com.example.googlelightcalendar.ui_components.calendar.DateSelector
import com.example.googlelightcalendar.ui_components.custom_column.AppColumnContainer
import com.example.googlelightcalendar.ui_components.dialog.ErrorAlertDialog
import com.example.googlelightcalendar.ui_components.menu.CustomDropDownMenu
import com.example.googlelightcalendar.ui_components.text_fields.CustomOutlineTextField
import kotlinx.coroutines.Dispatchers


@Composable
fun PhysicalDetailScreen() {
    val viewModel: PhysicalDetailsViewModel = hiltViewModel()
    BackHandler {
        viewModel.navigationManger.onBackPress()
    }

    PhysicalDetailContent(
        state = viewModel.state.collectAsState(Dispatchers.Main.immediate).value,
        navToRegisterGoals = viewModel::storePhysicalDetailsInCache,
        retry = viewModel::reset
    )
}


@Composable
@Preview
fun PhysicalDetailContent(
    @PreviewParameter(PhysicalDetailPreview::class)
    state: PhysicalDetailState.PhysicalDetails,
    navToRegisterGoals: (PhysicalDetailState.PhysicalDetails) -> Unit = {},
    retry: () -> Unit = {},
) {
    AppColumnContainer(
        modifier = Modifier
            .padding(20.dp)
            .fillMaxSize()
    ) {
        if (
            state.errorState.isError
        ) {
            ErrorAlertDialog(
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
            modifier = Modifier.fillMaxWidth(),
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
                modifier = Modifier.weight(5f),
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
            text = "Next 2/3",
            onClick = {
                navToRegisterGoals(state)
            },
        )
    }

}




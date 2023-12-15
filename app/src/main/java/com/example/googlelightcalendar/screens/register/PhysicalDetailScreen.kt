package com.example.googlelightcalendar.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.example.googlelightcalendar.core.registration.PhysicalDetailState
import com.example.googlelightcalendar.core.registration.PhysicalDetailsViewModel
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

    PhysicalDetailContent(
        state = viewModel.state.collectAsState(Dispatchers.Main.immediate).value,
        navToRegisterGoals = viewModel::storePhysicalDetailsInCahce,
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
        modifier = Modifier.padding(
            20.dp,
        )
    ) {
        if (
            state.containsError
        ) {
            ErrorAlertDialog(
                title = "Error",
                error = "Failed login in",
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
                value = state.weight.value ?: "",
                onValueChange = { weight ->
                    state.weight.value = weight
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            )

            CustomDropDownMenu(
                modifier = Modifier
                    .padding(
                        horizontal = 10.dp,
                    )
                    .weight(2f),
                options = listOf("kg", "lb"),
                onOptionChange = {
                    state.weightUnit.value = it
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
                value = state.height.value ?: " ",
                onValueChange = { height ->
                    state.height.value = height
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            )

            CustomDropDownMenu(
                modifier = Modifier
                    .padding(
                        horizontal = 10.dp,
                    )
                    .weight(2f),
                options = listOf("ft", "in"),
                onOptionChange = { unit ->
                    state.height.value = unit
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

        val genderOptions = listOf("Male", "Female", "Other")

        LaunchedEffect(key1 = state.selectedGender) {
            state.gender.value = when (state.selectedGender.value) {
                0 -> {
                    Genders.Male
                }

                1 -> {
                    Genders.FEMALE
                }

                3 -> {
                    Genders.OTHER
                }
                else -> { null}
            }
        }


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
                    selected = state.selectedGender.value == 0,
                    onClick = {
                        state.selectedGender.value = 0
                    },
                )
                Text(
                    text = genderOptions[0],
                    color = Color.White
                )
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = state.selectedGender.value == 1,
                    onClick = {
                        state.selectedGender.value = 1
                    },
                )
                Text(
                    text = genderOptions[1],
                    color = Color.White,
                )
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                RadioButton(
                    selected = state.selectedGender.value == 2,
                    onClick = {
                        state.selectedGender.value = 2
                    },
                )
                Text(
                    text = genderOptions[2],
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




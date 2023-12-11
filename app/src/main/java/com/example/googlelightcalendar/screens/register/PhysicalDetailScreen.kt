package com.example.googlelightcalendar.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.core.registration.PhysicalDetailsViewModel
import com.example.googlelightcalendar.ui_components.buttons.StandardButton
import com.example.googlelightcalendar.ui_components.calendar.MyDatePickerDialog
import com.example.googlelightcalendar.ui_components.custom_column.AppColumnContainer
import com.example.googlelightcalendar.ui_components.menu.CustomDropDownMenu
import com.example.googlelightcalendar.ui_components.text_fields.CustomOutlineTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhysicalDetailContent(
    viewModel: PhysicalDetailsViewModel = hiltViewModel()
) {
    AppColumnContainer(
        modifier = Modifier.padding(
            20.dp,
        )
    ) {

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

        MyDatePickerDialog(
            modifier = Modifier.fillMaxWidth()
        )

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "MM/DD/YYYY",
            textAlign = TextAlign.Start,
            color = Color.White,
            fontSize = TextUnit(
                value = 16f,
                type = TextUnitType.Sp
            )
        )



        Spacer(modifier = Modifier.size(30.dp))


        Row(
            modifier = Modifier.padding(top = 8.dp)
        ) {

            CustomOutlineTextField(
                modifier = Modifier.weight(5f),
                value = "",

                onValueChange = {},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            CustomDropDownMenu(
                modifier = Modifier
                    .padding(
                        horizontal = 10.dp,
                    )
                    .weight(2f),
                options = listOf("kg", "lb"),
            )
        }


        Spacer(modifier = Modifier.size(30.dp))
        Row(
            modifier = Modifier.padding(top = 8.dp)
        ) {

            CustomOutlineTextField(
                modifier = Modifier.weight(5f),
                value = "",

                onValueChange = {},
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )

            CustomDropDownMenu(
                modifier = Modifier
                    .padding(
                        horizontal = 10.dp,
                    )
                    .weight(2f),
                options = listOf("ft", "in"),
            )
        }

        Spacer(modifier = Modifier.size(30.dp))

        val genderOptions  = listOf("Male", "Female", "Other")


        Row {

        }

        StandardButton(
            modifier = Modifier
                .align(
                    Alignment.End
                )
                .fillMaxWidth(),
            text = "next 2/3",
            onClick = { viewModel.navToRegisterGoals() },
        )
    }
}




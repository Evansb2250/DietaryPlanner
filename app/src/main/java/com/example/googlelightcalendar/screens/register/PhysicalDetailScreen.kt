package com.example.googlelightcalendar.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.core.registration.PhysicalDetailsViewModel
import com.example.googlelightcalendar.ui_components.calendar.MyDatePickerDialog
import com.example.googlelightcalendar.ui_components.custom_text.CustomTextHeader
import com.example.googlelightcalendar.ui_components.menu.CustomDropDownMenu


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhysicalDetailContent(
    viewModel: PhysicalDetailsViewModel = hiltViewModel()
) {
    Scaffold { it ->
        Column(
            modifier = Modifier.padding(
                20.dp,
            )
        ) {
            CustomTextHeader(
                modifier = Modifier.padding(16.dp), text = "Register your data"
            )

            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(
                    id = R.drawable.user_info_icon,
                ),
                contentDescription = "",
            )

            Column(
                modifier = Modifier.padding(
                    24.dp
                )
            ) {
                CustomDropDownMenu(
                    options = listOf("None", "Male", "Female", "Other")
                )
                Spacer(modifier = Modifier.size(8.dp))
                MyDatePickerDialog(
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.size(8.dp))
                Row(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(5f),
                        value = "",
                        onValueChange = {},
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Button(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .height(40.dp)
                            .width(40.dp)
                            .weight(3f),
                        onClick = {},
                        shape = RectangleShape,
                    ) {
                        Text(text = "kg")
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
                Row(
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    OutlinedTextField(
                        modifier = Modifier.weight(5f),
                        value = "",
                        onValueChange = {},
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        )
                    )
                    Button(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .height(40.dp)
                            .width(40.dp)
                            .weight(3f),
                        onClick = {},
                        shape = RectangleShape,
                    ) {
                        Text(text = "feet")
                    }
                }
                Spacer(modifier = Modifier.size(8.dp))
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = { viewModel.navToRegisterGoals() }) {
                Text(text = "next 2/3")
            }
    }
}
}




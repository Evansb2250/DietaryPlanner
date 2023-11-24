package com.example.googlelightcalendar.screens.register

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.ui_components.calendar.MyDatePickerDialog
import com.example.googlelightcalendar.ui_components.menu.CustomDropDownMenu

@Composable
fun PhysicalDetailScreen() {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhysicalDetailContent() {
    Scaffold { it ->
        Column(
            modifier = Modifier.padding(
                20.dp,
            )
        ) {
            HeaderComponent(
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
                MyDatePickerDialog(
                    modifier = Modifier.fillMaxWidth()
                )

            }
//
//            CustomOutlineTextField(
//                label = "gender",
//                text = "",
//                onTextChange = {}
//            )


//
//            CustomOutlineTextField(
//                label = "gender",
//                text = "",
//                onTextChange = {}
//            )
//
//            CustomOutlineTextField(
//                label = "gender",
//                text = "",
//                onTextChange = {}
//            )
//            CustomOutlineTextField(
//                label = "gender",
//                text = "",
//                onTextChange = {}
//            )
        }
    }
}


@Composable
fun HeaderComponent(
    modifier: Modifier,
    text: String,
) {
    Text(
        modifier = modifier
            .fillMaxWidth(),
        text = text,
        textAlign = TextAlign.Center,
        fontSize = TextUnit(
            value = 32f,
            type = TextUnitType.Sp
        )
    )
}


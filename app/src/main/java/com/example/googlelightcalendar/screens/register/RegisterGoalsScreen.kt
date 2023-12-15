package com.example.googlelightcalendar.screens.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.googlelightcalendar.ui_components.custom_checkbox.CustomCheckBoxField
import com.example.googlelightcalendar.ui_components.custom_text.CustomTextHeader


@Preview(showBackground = true)
@Composable
fun RegisterGoalsScreen() {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        CustomTextHeader(
            modifier = Modifier.fillMaxWidth(),
            text = "What do you want to achieve",
        )

        CustomCheckBoxField(
            value = "Loose Weight"
        )

        CustomCheckBoxField(
            value = "Gain Weight"
        )

        CustomCheckBoxField(
            value = "Maintain Weight"
        )

        CustomCheckBoxField(
            value = "Gain Muscle"
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { /*TODO*/ }) {
            Text("Click to confirm")
        }
    }
}




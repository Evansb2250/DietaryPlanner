package com.example.googlelightcalendar.ui_components.textfields

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomOutlineTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String? = null
){
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            if(label != null){
                Text(
                    modifier = Modifier.background(
                        color = Color.White,
                    ),
                    text = label,
                    color = Color.Black,
                )
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .background(
                Color.White,
                RoundedCornerShape(5.dp)
            ),
        shape = RoundedCornerShape(5.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
        ),
    )
}

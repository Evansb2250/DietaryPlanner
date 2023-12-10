package com.example.googlelightcalendar.ui_components.text_fields

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.googlelightcalendar.common.imageHolder
import com.example.googlelightcalendar.ui.theme.appColor

@Composable
fun CustomOutlineTextField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {},
    label: String? = null,
    enabled: Boolean = true,
    readOnly: Boolean = false,
    leadingIcon: imageHolder? = null,
    shape: Shape = RoundedCornerShape(4.dp)
) {
    OutlinedTextField(
        modifier = modifier
            .height(
                60.dp
            )
            .fillMaxWidth()
            .background(
                Color.White,
                shape,
            ),
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            if (leadingIcon != null) {
                Image(
                    painter = painterResource(
                        id = leadingIcon.leadingIcon,
                    ), contentDescription = leadingIcon.description
                )
            }
        },
        label = {
            if (label != null) {
                Text(
                    modifier = Modifier.background(
                        color = Color.White,
                    ),
                    text = label,
                    color = appColor,
                )
            }
        },
        maxLines = 1,
        readOnly = readOnly,
        enabled = enabled,
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.White,
        ),
    )
}




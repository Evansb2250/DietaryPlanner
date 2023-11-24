package com.example.googlelightcalendar.ui_components.text_fields

import androidx.compose.foundation.Image
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.example.googlelightcalendar.common.imageHolder

@Composable
fun CustomOutlineTextField(
    leadingIcon: imageHolder? = null,
    label: String? = null,
    text: String,
    onTextChange: (String) -> Unit,
) {

    OutlinedTextField(
        leadingIcon = {
            if (leadingIcon != null) {
                Image(
                    painter = painterResource(
                        id = leadingIcon.leadingIcon,
                    ), contentDescription = leadingIcon.description
                )
            }
        },
        value = text,
        onValueChange = onTextChange,
        label = {
            if (label != null) {
                Text(
                    text = label
                )
            }
        },
    )
}

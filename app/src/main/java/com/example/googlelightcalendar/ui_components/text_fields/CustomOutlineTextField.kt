package com.example.googlelightcalendar.ui_components.text_fields

import androidx.compose.foundation.Image
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.example.googlelightcalendar.common.imageHolder

@Composable
fun CustomOutlineTextField(
    modifier: Modifier = Modifier,
    leadingIcon: imageHolder? = null,
    label: String? = null,
    text: String,
    enabled: Boolean = true,
    readOnly:Boolean = false,
    onTextChange: (String) -> Unit,
) {

    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        readOnly = readOnly,
        modifier = modifier,
        leadingIcon = {
            if (leadingIcon != null) {
                Image(
                    painter = painterResource(
                        id = leadingIcon.leadingIcon,
                    ), contentDescription = leadingIcon.description
                )
            }
        },
        enabled = enabled,
        label = {
            if (label != null) {
                Text(
                    text = label
                )
            }
        },
    )
}
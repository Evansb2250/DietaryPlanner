package com.example.chooseu.ui.ui_components.text_fields

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.example.chooseu.utils.NumberUtils

@Composable
fun NumberTextField(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {
    CustomOutlineTextField(
        modifier = modifier,
        value = value,
        onValueChange = {
            onValueChange(NumberUtils.updateStringToValidNumber(it))
        },
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
    )
}
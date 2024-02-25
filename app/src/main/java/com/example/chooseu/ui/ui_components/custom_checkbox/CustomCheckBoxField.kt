package com.example.chooseu.ui.ui_components.custom_checkbox

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextAlign

sealed class CheckBoxOrientation() {
    object ColumnCheckBox : CheckBoxOrientation()
    object HorizontalCheckBox : CheckBoxOrientation()
}


@Composable
fun CustomCheckBoxField(
    enabled: Boolean = false,
    readOnly: Boolean = false,
    value: String,
    textAlign: TextAlign = TextAlign.Start,
    checkBoxOrientation: CheckBoxOrientation = CheckBoxOrientation.HorizontalCheckBox,
) {
    when (checkBoxOrientation) {
        CheckBoxOrientation.ColumnCheckBox -> {
            ColumnLandscape(
                enabled = enabled,
                readOnly = readOnly,
                value = value,
                textAlign = textAlign,
            )
        }

        CheckBoxOrientation.HorizontalCheckBox -> {
            horizontalLandscape(
                enabled = enabled,
                readOnly = readOnly,
                value = value,
                textAlign = textAlign,
            )
        }
    }
}


@Composable
private fun horizontalLandscape(
    enabled: Boolean,
    readOnly: Boolean,
    value: String,
    textAlign: TextAlign,
) {
    Row() {
        TextField(
            enabled = enabled,
            readOnly = readOnly,
            value = value,
            onValueChange = {},
            leadingIcon = {
                Checkbox(
                    checked = false, onCheckedChange = {})
            },
            textStyle = LocalTextStyle.current.copy(
                textAlign = textAlign,
            ),
        )
    }
}

@Composable
private fun ColumnLandscape(
    enabled: Boolean,
    readOnly: Boolean,
    value: String,
    textAlign: TextAlign,
) {
    Column {
        OutlinedTextField(
            enabled = enabled,
            readOnly = readOnly,
            value = value,
            onValueChange = {},
            leadingIcon = {
                Checkbox(
                    checked = false,
                    onCheckedChange = {},
                )
            },
            textStyle = LocalTextStyle.current.copy(
                textAlign = textAlign,
            ),
        )
    }
}
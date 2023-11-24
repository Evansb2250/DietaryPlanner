package com.example.googlelightcalendar.ui_components.text_fields

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.googlelightcalendar.R

@Composable
fun CustomPasswordTextField(
    text: String,
    onTextChange: (String) -> Unit
) {
    var showPassword: Boolean by rememberSaveable {
        mutableStateOf(false)
    }

    OutlinedTextField(
        leadingIcon = {
            Image(
                painter = painterResource(
                    id = R.drawable.password_icon,
                ), contentDescription = "icon of a lock"
            )

        },
        trailingIcon = {

            val trailingIcon = if (!showPassword) {
                R.drawable.hide_password_icon to "hide password"
            } else {
                R.drawable.show_password_icon to "show password"
            }

            Image(
                modifier = Modifier.clickable {
                    showPassword = !showPassword
                },

                painter = painterResource(
                    id = trailingIcon.first,
                ),
                contentDescription = trailingIcon.second,
            )

        },
        value = text,
        onValueChange = onTextChange,
        label = {
            Text(
                text = "password"
            )
        },
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
    )

}

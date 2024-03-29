package com.example.chooseu.ui.ui_components.text_fields

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.chooseu.R
import com.example.chooseu.common.roundedShapeDp
import com.example.chooseu.ui.theme.appColor

@Composable
fun CustomPasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    textColor: Color = appColor,
    modifier: Modifier = Modifier,
    unFocusBorderColor: Color = Color.White,
    backGroundColor: Color = Color.White,
    onFocusBorderColor: Color = Color.White,
    shape: Shape = RoundedCornerShape(roundedShapeDp),
) {
    var showPassword: Boolean by rememberSaveable {
        mutableStateOf(false)
    }


    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                modifier = Modifier.background(
                    color = backGroundColor,
                ),
                text = "Password",
                color = textColor,
            )
        },
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
        modifier = modifier
            .fillMaxWidth()
            .height(
                60.dp
            )
            .background(
                Color.White,
                shape,
            ),
        maxLines = 1,
        shape = shape,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = onFocusBorderColor,
            unfocusedBorderColor = unFocusBorderColor,
        ),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
    )
}

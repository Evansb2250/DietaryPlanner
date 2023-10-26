package com.example.googlelightcalendar.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.googlelightcalendar.viewmodels.LoginScreenStates

@Composable
fun LoginScreen(
    state: LoginScreenStates.LoginScreenState,
    initiateLogin: () -> Unit = {},
    getCalender: () -> Unit = {},
    signOut: () -> Unit = {},
) {
    LoginContent(
        initiateGoogleSignIn = initiateLogin
    )
}

@Composable
fun LoginContent(
    initiateGoogleSignIn:() -> Unit,
) {
    LoginBottomSheet(
        initiateGoogleSignIn = initiateGoogleSignIn
    )
}


val sidePadding = 16.dp

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginBottomSheet(
    initiateGoogleSignIn: () -> Unit = {},
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    ModalBottomSheet(
        modifier = Modifier.wrapContentWidth(),
        sheetState = bottomSheetScaffoldState.bottomSheetState,
        onDismissRequest = { /*TODO*/ },
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = sidePadding
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Pheonix",
                textAlign = TextAlign.Center,
            )
            Spacer(
                modifier = Modifier.size(30.dp)
            )

            OutlinedTextField(
                value = "dsadsa",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            )

            Spacer(
                modifier = Modifier.size(30.dp)
            )

            OutlinedTextField(
                value = "dsadsa",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp)
            )
            Spacer(
                modifier = Modifier.size(20.dp)
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = { /*TODO*/ },
            ) {
                Text(
                    text = "Log in"
                )
            }

            Button(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                onClick = { /*TODO*/ }
            ) {
                Text(
                    text = "Create account"
                )
            }

            Spacer(
                modifier = Modifier.size(20.dp)
            )

            Divider(
                modifier = Modifier.wrapContentWidth()
            )

            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = "Or"
            )

            Spacer(
                modifier = Modifier.size(20.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
            ) {
                OutlinedButton(
                    shape = RoundedCornerShape(10.dp),
                    onClick = { /*TODO*/ },
                ) {
                    Text(text = "FaceBook")
                }
                Spacer(
                    modifier = Modifier.size(10.dp)
                )
                OutlinedButton(
                    shape = RoundedCornerShape(10.dp),
                    onClick = initiateGoogleSignIn,
                ) {
                    Text(text = "Google")
                }
            }
            Spacer(
                modifier = Modifier.size(10.dp)
            )
        }
    }


}

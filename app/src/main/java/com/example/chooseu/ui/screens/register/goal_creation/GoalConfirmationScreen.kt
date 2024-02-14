package com.example.chooseu.ui.screens.register.goal_creation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.chooseu.R
import com.example.chooseu.common.sidePadding
import com.example.chooseu.ui.theme.appColor
import com.example.chooseu.ui.ui_components.buttons.StandardButton

@Preview(
    showBackground = true,
)
@Composable
fun GoalConfirmationScreen(
    createAccount: () -> Unit = {},
    userData: List<String> = emptyList(),
) {
    Box(
        modifier = Modifier
            .background(
                color = appColor,
            )
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        Image(
            modifier = Modifier.padding(
                all = sidePadding
            ),
            painter = painterResource(
                id = R.drawable.confirmation_circle,
            ),
            contentDescription = ""
        )
        Box(
            modifier = Modifier
                .background(
                    color = Color.Transparent,
                )
                .fillMaxSize(),
            contentAlignment = Alignment.CenterStart,
        ) {
            LazyColumn(
            ) {
                items(
                    userData.size
                ) { index ->
                    Text(
                        modifier = Modifier
                            .padding(
                                horizontal = sidePadding,
                            )
                            .fillMaxWidth(),
                        text = userData[index],
                        textAlign = TextAlign.Left,
                        color = Color.White,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .background(
                    color = Color.Transparent,
                )
                .fillMaxSize(),
            contentAlignment = Alignment.BottomCenter,
        ) {
            StandardButton(
                modifier = Modifier.padding(
                    all = sidePadding
                ),
                text = "Create Account",
                onClick = createAccount,
            )
        }
    }
}
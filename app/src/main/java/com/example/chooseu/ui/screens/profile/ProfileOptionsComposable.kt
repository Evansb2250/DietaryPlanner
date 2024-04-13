package com.example.chooseu.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.chooseu.ui.screens.profile.states.ProfileScreenStates
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.ui.theme.appColor
import com.example.chooseu.ui.ui_components.custom_layout.CustomRowLayout


@Composable
fun ProfileSettingsContainer(
    settings:  List<ProfileScreenStates.ProfilePage.ProfileOptions>,
    paddingValues: PaddingValues,
    navigate: (GeneralDestinations) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .padding(paddingValues)
            .background(
                color = appColor,
            ).fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .padding(
                    vertical = 16.dp,
                )
                .wrapContentHeight(),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            settings.forEach {
                CustomRowLayout(
                    leadingIcon = {
                        Image(
                            painter = painterResource(
                                id = it.leadingIconId
                            ),
                            contentDescription = ""
                        )
                    },
                    title = {
                        Text(
                            color = Color.White,
                            text = it.text,
                        )
                    },
                    trailingIcon = {
                        Image(
                            painter = painterResource(
                                id = it.trailingIconId
                            ),
                            contentDescription = ""
                        )
                    },
                    onClick = {
                        navigate(it.destination)
                    }
                )
                Spacer(modifier = Modifier.size(25.dp))
            }
        }
    }
}
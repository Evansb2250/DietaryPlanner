package com.example.chooseu.ui.ui_components.toolbar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chooseu.core.toolbar_states.LeadingIcon
import com.example.chooseu.core.toolbar_states.ToolBarState
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.ui.theme.appColor

@Composable
fun ChooseUToolBar(
    toolBarState: ToolBarState,
    navigateBack: () -> Unit,
    navigateToActionDestination: (GeneralDestinations) -> Unit,
) {
    Row(
        modifier = Modifier.background(
            color = appColor,
        ),
    ) {
        when (toolBarState) {
            is ToolBarState.Navigated -> {
                ChooseUToolBarContent(
                    leadingIcon = toolBarState.leadingIcon,
                    headline = toolBarState.headline,
                    navigateBack = navigateBack,
                    trailingIcon = {
                        Image(
                            modifier = Modifier.clickable(
                                onClick = { navigateToActionDestination(toolBarState.trailingIcon.destinations) }
                            ),
                            painter = painterResource(
                                id = toolBarState.trailingIcon.drawable,
                            ),
                            contentDescription = ""
                        )
                    },
                )
            }

            is ToolBarState.Home -> {
                ChooseUToolBarContent(
                    leadingIcon = toolBarState.leadingIcon,
                    navigateBack = navigateBack,
                    trailingIcon = {
                        if (toolBarState.showTrailingIcon) {
                            //TODO add code to have badge icon.
                            Image(
                                modifier = Modifier.clickable(
                                    onClick = { navigateToActionDestination(toolBarState.trailingIcon.destinations) }
                                ),
                                painter = painterResource(
                                    id = toolBarState.trailingIcon.drawable,
                                ),
                                contentDescription = ""
                            )
                        }
                    },
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseUToolBarContent(
    leadingIcon: LeadingIcon,
    headline: String = "",
    trailingIcon: @Composable () -> Unit,
    navigateBack: () -> Unit
) {
    TopAppBar(
        modifier = Modifier.padding(
            horizontal = 12.dp,
        ),
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = appColor,
        ),
        navigationIcon = {
            Image(
                modifier = Modifier.clickable(
                    enabled = leadingIcon.clickable,
                    onClick = navigateBack
                ),
                painter = painterResource(
                    id = leadingIcon.drawable,
                ),
                contentDescription = ""
            )
        },
        title = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    color = Color.White,
                    text = headline,
                    textAlign = TextAlign.Center,
                )
            }
        },
        actions = {
            trailingIcon()
        }
    )
}
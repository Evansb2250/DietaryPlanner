package com.example.googlelightcalendar.screens.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.core.profile_screen.ProfileViewModel
import com.example.googlelightcalendar.core.toolBarStates.ToolBarState
import com.example.googlelightcalendar.ui.theme.appColor
import com.example.googlelightcalendar.ui_components.custom_layout.CustomRowLayout
import com.example.googlelightcalendar.ui_components.toolbar.ChooseUToolBar

@Preview(
    showBackground = true
)
@Composable
fun ProfileScreen(
) {
    BackHandler {

    }


    val vm: ProfileViewModel = hiltViewModel()
    val state = vm.state.collectAsState().value

    //Used to prevent taping more than one option at a time.
    val unlocked = rememberSaveable {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = unlocked) {
        if (!unlocked.value) {
            unlocked.value = true
        }
    }
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = appColor,
            ),
        topBar = {
            ChooseUToolBar(
                toolBarState = ToolBarState.Home(showTrailingIcon = false),
                navigateBack = { /*TODO*/ },
                navigateToActionDestination = {}
            )
        }
    ){ it ->
        Box(
            modifier = Modifier
                .padding(it)
                .background(
                    color = appColor,
                ),
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
                state.items.forEach {
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
                        }
                    ) {
                        if (unlocked.value) {
                            vm.navigate(it.destination)
                            unlocked.value = false
                        }
                    }
                    Spacer(modifier = Modifier.size(25.dp))
                }
            }
            Box(
                modifier = Modifier
                    .padding(
                        vertical = 16.dp
                    )
                    .fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                OutlinedButton(
                    modifier = Modifier
                        .height(
                            52.dp,
                        )
                        .fillMaxWidth(),
                    onClick = vm::logout
                ) {
                    Image(
                        modifier = Modifier.padding(
                            horizontal = 4.dp,
                        ),
                        painter = painterResource(
                            id = R.drawable.exit_icon,
                        ),
                        contentDescription = ""
                    )
                    Text(
                        modifier = Modifier
                            .align(
                                alignment = Alignment.CenterVertically
                            ),
                        color = Color.White,
                        text = "Sign Out",
                    )
                }
            }
        }
    }

}



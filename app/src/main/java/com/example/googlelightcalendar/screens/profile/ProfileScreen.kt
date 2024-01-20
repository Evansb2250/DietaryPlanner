package com.example.googlelightcalendar.screens.profile

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.screens.loginScreen.sidePadding
import com.example.googlelightcalendar.ui.theme.appColor
import com.google.android.datatransport.runtime.Destination

@Preview(
    showBackground = true
)
@Composable
fun ProfileScreen(
    logout: () -> Unit = {},
) {
    BackHandler {

    }


    Box(
        modifier = Modifier
            .background(
                color = appColor,
            )
            .padding(sidePadding)
            .fillMaxSize(),
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
            items.forEach {
                CustomBoxLayout(
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
                ){
                    it.destination
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
                onClick = logout
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


@Composable
fun CustomBoxLayout(
    leadingIcon: @Composable () -> Unit = {},
    title: @Composable () -> Unit = {},
    trailingIcon: @Composable () -> Unit = {},
    onClick: () -> Unit = {},
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier.weight(
                1f,
            ),
            contentAlignment = Alignment.Center,
        ) {
            leadingIcon()
        }
        Box(
            modifier = Modifier.weight(
                3f,
            ),
        ) {
            title()
        }
        Box(
            modifier = Modifier.weight(
                1f,
            ).clickable {
               onClick
            },
            contentAlignment = Alignment.CenterEnd,
        ) {
            trailingIcon()
        }
    }
}


data class ProfileOptions(
    val leadingIconId: Int,
    val text: String,
    val trailingIconId: Int,
    val destination: Destination? = null,
)


val items = listOf(
    ProfileOptions(
        leadingIconId = R.drawable.profile_icon_2,
        text = "Account",
        trailingIconId = R.drawable.right_arrow
    ),

    ProfileOptions(
        leadingIconId = R.drawable.notification_icon_2,
        text = "Notification",
        trailingIconId = R.drawable.right_arrow
    ),

    ProfileOptions(
        leadingIconId = R.drawable.calendar_icon_2,
        text = "Calendar",
        trailingIconId = R.drawable.right_arrow
    ),

    ProfileOptions(
        leadingIconId = R.drawable.lock_icon,
        text = "Terms of Service",
        trailingIconId = R.drawable.right_arrow
    )


)
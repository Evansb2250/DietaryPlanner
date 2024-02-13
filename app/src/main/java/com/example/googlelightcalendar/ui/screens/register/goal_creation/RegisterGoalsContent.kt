package com.example.googlelightcalendar.ui.screens.register.goal_creation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.core.registration.state.RegisterGoalStates
import com.example.googlelightcalendar.ui.ui_components.custom_column.AppColumnContainer

@Composable
fun RegisterGoalsContent(
    state: RegisterGoalStates.GoalSelectionState,
    onCreateAccount: (RegisterGoalStates.GoalSelectionState) -> Unit = {},
) {

    AppColumnContainer(
        modifier = Modifier
            .fillMaxSize(),
        disableBackPress = false,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {

        Image(
            modifier = Modifier
                .fillMaxWidth()
                .align(
                    alignment = Alignment.CenterHorizontally,
                ),
            painter = painterResource(
                id = R.drawable.main_logo,
            ),
            contentDescription = "Logo"
        )

        Spacer(
            modifier = Modifier.size(30.dp),
        )

        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = "What do you want to achieve?",
            color = Color.White,
            textAlign = TextAlign.Left,
        )

        Spacer(
            modifier = Modifier.size(30.dp),
        )

        LazyColumn(
            modifier = Modifier
                .weight(10f)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
        ) {

            items(
                state.goals.size
            ) { index ->
                Column {
                    AsyncImage(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp)),
                        model = state.goals[index].imageUri,
                        contentDescription = state.goals[index].imageDescription
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start,
                    ) {
                        Checkbox(
                            checked = state.selectedGoal.value == state.goals[index],
                            onCheckedChange = { state.selectedGoal.value = state.goals[index] },
                        )
                        Text(
                            text = state.goals[index].toString(),
                            color = Color.White,
                        )
                    }
                }

                Spacer(modifier = Modifier.size(30.dp))
            }
        }

        if (state.selectedGoal.value != null) {
            GoalsDialog(
                state = state,
                onDismiss = {
                    state.selectedGoal.value = null
                    state.clear()
                },
                onCreateAccount = onCreateAccount,
            )
        }
    }
}

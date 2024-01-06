package com.example.googlelightcalendar.screens.register

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.core.registration.RegisterGoalViewModel
import com.example.googlelightcalendar.core.registration.state.RegisterGoalStates.AccountComfirmationState
import com.example.googlelightcalendar.core.registration.state.RegisterGoalStates.GoalSelectionState
import com.example.googlelightcalendar.core.registration.state.weightUnits
import com.example.googlelightcalendar.screens.loginScreen.sidePadding
import com.example.googlelightcalendar.ui.theme.appColor
import com.example.googlelightcalendar.ui_components.buttons.StandardButton
import com.example.googlelightcalendar.ui_components.calendar.DateSelector
import com.example.googlelightcalendar.ui_components.custom_column.AppColumnContainer
import com.example.googlelightcalendar.ui_components.menu.CustomDropDownMenu
import com.example.googlelightcalendar.ui_components.text_fields.CustomOutlineTextField


@Preview(showBackground = true)
@Composable
fun RegisterGoalsScreen() {

    val viewModel = hiltViewModel<RegisterGoalViewModel>()
    val state = viewModel.state.collectAsStateWithLifecycle().value

    when (
        state
    ) {
        is AccountComfirmationState -> {
            confirmationPage(
                userData = state.registrationInfoList,
            )
        }

        is GoalSelectionState -> {
            RegisterGoalsContent(
                state = state,
                onCreateAccount = viewModel::initiateAccountCreation
            )
        }
    }

}

@Preview(
    showBackground = true,
)
@Composable
fun confirmationPage(
    userData: List<String> = emptyList()
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
                onClick = { },
            )
        }
    }
}

@Composable
private fun RegisterGoalsContent(
    state: GoalSelectionState,
    onCreateAccount: (GoalSelectionState) -> Unit = {},
) {

    AppColumnContainer(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
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
                        modifier = Modifier.size(
                            width = 318.dp,
                            height = 249.dp
                        ),
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


@Composable
fun GoalsDialog(
    state: GoalSelectionState,
    onDismiss: () -> Unit,
    onCreateAccount: (GoalSelectionState) -> Unit,
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = appColor,
                    shape = RoundedCornerShape(25.dp)
                )
                .wrapContentSize(),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.padding(
                    horizontal = 16.dp,
                )
            ) {

                Spacer(modifier = Modifier.size(10.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    Image(
                        painter = painterResource(
                            id = R.drawable.goals_icon
                        ),
                        contentDescription = "Goals",
                    )
                }
                if (
                    state.goalIntensityOptions.isNotEmpty()
                ) {

                    Text(
                        modifier = Modifier.padding(
                            all = 16.dp,
                        ),
                        text = "Choose your weekly goal.",
                        color = Color.White,
                        textAlign = TextAlign.Left,
                    )

                    CustomDropDownMenu(
                        modifier = Modifier
                            .height(60.dp)
                            .fillMaxWidth(),
                        selectedOptionText = state.goalIntensityText.value,
                        options = state.getCustomDropDownIntensityTextOption(),
                        onOptionChange = {
                            state.goalIntensityText.value = it
                        },
                        onIndexChange = { index ->
                            state.weeklyGoalIntensity.value = state.goalIntensityOptions[index]
                        }
                    )

                }

                Spacer(modifier = Modifier.size(20.dp))

                if (state.selectedGoal.value!!.containsWeightRequirement) {

                    Text(
                        modifier = Modifier.padding(
                            horizontal = 16.dp,
                        ),
                        text = state.selectedGoal.value?.goalPrompt ?: "error",
                        color = Color.White,
                        textAlign = TextAlign.Left,
                    )
                    Spacer(modifier = Modifier.size(20.dp))

                    Row(
                        modifier = Modifier
                            .padding(top = 8.dp)
                    ) {
                        CustomOutlineTextField(
                            modifier = Modifier.weight(5f),
                            value = state.targetWeight.value.weight,
                            onValueChange = { weight ->
                                state.targetWeight.value = state.targetWeight.value.copy(
                                    weight = weight,
                                )
                            },
                            label = "weight",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number
                            ),
                            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                        )

                        Spacer(modifier = Modifier.size(20.dp))

                        CustomDropDownMenu(
                            selectedOptionText = state.targetWeight.value.weightType.type,
                            modifier = Modifier
                                .padding(
                                    horizontal = 10.dp,
                                )
                                .size(
                                    width = 63.dp,
                                    height = 56.dp,
                                ),
                            options = weightUnits.map { it.type },
                            enable = state.missingUnitsOfWeight,
                            onIndexChange = { index ->
                                state.targetWeight.value.copy(
                                    weightType = weightUnits[index]
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.size(20.dp))

                Text(
                    text = if (state.selectedGoal.value!!.containsWeightRequirement) "By when?" else state.selectedGoal.value!!.goalPrompt,
                    color = Color.White,
                    textAlign = TextAlign.Left,
                )
                Spacer(modifier = Modifier.size(20.dp))

                DateSelector(
                    initialDate = state.dateToAccomplishGoalBy,
                    modifier = Modifier.fillMaxWidth(),
                    onDateChange = {
                        state.dateToAccomplishGoalBy.value = it
                    }
                )


                Spacer(modifier = Modifier.size(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    Button(
                        onClick = {
                            if (state.validateSetGoal()) {
                                Toast.makeText(context, "Creating Goal", Toast.LENGTH_LONG).show()
                                onCreateAccount(state)
                            } else {
                                if (state.errorStateInGoalDialog.value.isError) {
                                    Toast.makeText(
                                        context,
                                        state.errorStateInGoalDialog.value.message,
                                        Toast.LENGTH_LONG
                                    )
                                        .show()
                                }
                            }
                        },
                    ) {
                        Text(text = "Submit")
                    }
                    Spacer(
                        modifier = Modifier.size(20.dp),
                    )
                    Button(
                        onClick = onDismiss,
                    ) {
                        Text(text = "Cancel")
                    }
                }
                Spacer(modifier = Modifier.size(20.dp))
            }
        }

    }
}
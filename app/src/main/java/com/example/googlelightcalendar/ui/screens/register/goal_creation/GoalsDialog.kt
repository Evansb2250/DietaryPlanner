package com.example.googlelightcalendar.ui.screens.register.goal_creation

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.googlelightcalendar.R
import com.example.googlelightcalendar.core.registration.state.RegisterGoalStates
import com.example.googlelightcalendar.core.registration.state.weightUnits
import com.example.googlelightcalendar.ui.theme.appColor
import com.example.googlelightcalendar.ui_components.calendar.DateSelector
import com.example.googlelightcalendar.ui_components.menu.CustomDropDownMenu
import com.example.googlelightcalendar.ui_components.text_fields.CustomOutlineTextField

@Composable
fun GoalsDialog(
    state: RegisterGoalStates.GoalSelectionState,
    onDismiss: () -> Unit,
    onCreateAccount: (RegisterGoalStates.GoalSelectionState) -> Unit,
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
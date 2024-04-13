package com.example.chooseu.ui.screens.account_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chooseu.common.Constants.Companion.heightUnits
import com.example.chooseu.common.Constants.Companion.weightUnits
import com.example.chooseu.common.sidePadding
import com.example.chooseu.core.toolbar_states.ToolBarState
import com.example.chooseu.ui.screens.account_screen.state.AccountStates
import com.example.chooseu.ui.theme.yellowMain
import com.example.chooseu.ui.ui_components.buttons.StandardButton
import com.example.chooseu.ui.ui_components.custom_column.AppColumnContainer
import com.example.chooseu.ui.ui_components.dialog.ErrorDialog
import com.example.chooseu.ui.ui_components.dialog.LoadingDialog
import com.example.chooseu.ui.ui_components.menu.CustomDropDownMenu
import com.example.chooseu.ui.ui_components.text_fields.CustomOutlineTextField
import com.example.chooseu.ui.ui_components.toolbar.ChooseUToolBar

@Composable
fun AccountScreenContent(
    state: AccountStates,
    enableEditMode: (AccountStates.AccountInfo) -> Unit = {},
    showHistoryWeightHistory: () -> Unit = {},
    saveInformation: (AccountStates.AccountInfo) -> Unit = {},
    onBackPress: () -> Unit = {},
    backToAccountScreen: () -> Unit = {},
    cancel: () -> Unit = {},
    closeDialog: () -> Unit = {},
) {

    Scaffold(
        topBar = {
            ChooseUToolBar(
                showTrailingIcon = false,
                toolBarState = ToolBarState.Navigated(
                    state.title,
                ),
                navigateBack = onBackPress,
                navigateToActionDestination = {}
            )
        }
    ) { it ->
        AppColumnContainer(
            modifier = Modifier.padding(it),
        ) {
            when (state) {
                is AccountStates.Error -> ErrorDialog(
                    title = "Error detected",
                    error = state.error,
                    onDismiss = closeDialog
                )

                AccountStates.Loading -> LoadingDialog()
                is AccountStates.AccountInfo -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(20.dp)
                        ) {

                            AccountText(
                                label = "Name:",
                                text = "${state.currentUser.name} ${state.currentUser.lastName}"
                            )

                            AccountText(
                                label = "Date Of Birth:",
                                text = "${state.currentUser.birthdate}",
                            )
                            AccountText(
                                label = "Age:",
                                text = "${state.calculateAge()}",
                            )

                            AccountText(
                                label = "Gender:",
                                text = "${state.currentUser.gender}",
                            )
                            AccountText(
                                label = "Email:",
                                text = state.currentUser.email,
                            )

                            AccountEdibleFields(
                                readOnly = state.readOnly,
                                value = state.weight,
                                onValueChange = state::updateWeight,
                                textFieldLabel = "weight",
                                dropDownValue = state.weightMetric,
                                onDropDownChange = state::updateWeightMetric,
                                dropDownOptions = weightUnits.map { it.type }
                            )

                            AccountEdibleFields(
                                readOnly = state.readOnly,
                                value = state.height,
                                onValueChange = state::updateHeight,
                                textFieldLabel = "Height",
                                dropDownValue = state.heightMetric,
                                onDropDownChange = state::updateHeightMetric,
                                dropDownOptions = heightUnits.map { it.type }
                            )
                            Spacer(
                                modifier = Modifier.size(10.dp),
                            )
                            Text(
                                modifier = Modifier.clickable {
                                    showHistoryWeightHistory()
                                },
                                text = "Show all updates..",
                                color = yellowMain,
                            )

                        }
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            if (state.readOnly) {
                                StandardButton(
                                    text = "Update Metrics",
                                    onClick = {
                                        enableEditMode(state)
                                    }
                                )
                            } else {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Button(
                                        onClick = { saveInformation(state) }
                                    ) {
                                        Text(text = "save")
                                    }

                                    Button(
                                        onClick = cancel
                                    ) {
                                        Text(text = "cancel")
                                    }
                                }
                            }
                        }
                    }
                }

                is AccountStates.BodyMassIndexHistory -> {
                    BackHandler {
                        backToAccountScreen()
                    }
                    LazyColumn {
                        items(
                            items = state.weightHistory,
                        ) {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = sidePadding,
                                        vertical = 4.dp,
                                    ),

                                ) {

                                Column(
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp,
                                    ),
                                ) {
                                    Text(
                                        text = it.date
                                    )
                                    Text(
                                        text = "${it.height} ${it.heightMetric}"
                                    )
                                    Text(
                                        text = "${it.weight} ${it.weightMetric}"
                                    )
                                }

                            }

                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun AccountText(
    label: String,
    text: String
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = label,
            color = Color.White,
        )
        Text(
            text = text,
            color = yellowMain,
        )
    }

}


@Composable
private fun AccountEdibleFields(
    modifier: Modifier = Modifier,
    readOnly: Boolean,
    value: String,
    onValueChange: (String) -> Unit = {},
    textFieldLabel: String,
    dropDownValue: String,
    onDropDownChange: (String) -> Unit = {},
    dropDownOptions: List<String>,
) {
    Row(
        modifier = Modifier.padding(top = 4.dp)
    ) {

        CustomOutlineTextField(
            modifier = modifier
                .weight(5f),
            value = value,
            onValueChange = onValueChange,
            label = textFieldLabel,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            readOnly = readOnly,
            textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
        )

        CustomDropDownMenu(
            selectedOptionText = dropDownValue,
            modifier = Modifier
                .padding(
                    horizontal = 10.dp,
                )
                .size(
                    width = 63.dp,
                    height = 56.dp,
                )
                .weight(2f),
            enable = !readOnly,
            options = dropDownOptions,
            onOptionChange = onDropDownChange
        )
    }
}
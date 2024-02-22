package com.example.chooseu.ui.screens.account_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.chooseu.core.AccountStates
import com.example.chooseu.core.toolbar_states.ToolBarState
import com.example.chooseu.ui.ui_components.custom_column.AppColumnContainer
import com.example.chooseu.ui.ui_components.dialog.ErrorDialog
import com.example.chooseu.ui.ui_components.dialog.LoadingDialog
import com.example.chooseu.ui.ui_components.text_fields.CustomOutlineTextField
import com.example.chooseu.ui.ui_components.toolbar.ChooseUToolBar

@Composable
fun AccountScreenContent(
    state: AccountStates,
    onBackPress: () -> Unit = {}
){

    Scaffold(
        topBar = {
            ChooseUToolBar(
                toolBarState = ToolBarState.Navigated(
                    "Account"
                ),
                navigateBack = onBackPress,
                navigateToActionDestination = {}
            )
        }
    ) { it ->
        AppColumnContainer(
            modifier = Modifier.padding(it),
        ) {
            when(state){
                is AccountStates.Failed -> ErrorDialog(title = "Error detected", error = state.error)
                AccountStates.Loading -> LoadingDialog()
                is AccountStates.Success -> {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        CustomOutlineTextField(value = state.currentUser.email)
                        CustomOutlineTextField(value = state.currentUser.name)
                        CustomOutlineTextField(value = state.currentUser.lastName)
                        CustomOutlineTextField(value = state.currentUser.gender)
                        CustomOutlineTextField(value = state.currentUser.birthdate)
                        CustomOutlineTextField(value = "${state.currentUser.weight} ${state.currentUser.weightMetric} ")
                        CustomOutlineTextField(value = "${state.currentUser.height} ${state.currentUser.heightMetric} ")
                    }
                }
            }

        }
    }

}
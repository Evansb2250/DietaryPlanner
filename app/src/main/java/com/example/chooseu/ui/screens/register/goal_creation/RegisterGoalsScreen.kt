package com.example.chooseu.ui.screens.register.goal_creation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chooseu.core.registration.RegisterGoalViewModel
import com.example.chooseu.core.registration.state.RegisterGoalStates
import com.example.chooseu.core.registration.state.RegisterGoalStates.AccountComfirmationState
import com.example.chooseu.ui.theme.appColor
import com.example.chooseu.ui.ui_components.dialog.ErrorDialog
import com.example.chooseu.ui.ui_components.dialog.LoadingDialog


@Preview(showBackground = true)
@Composable
fun RegisterGoalsScreen(
    viewModel: RegisterGoalViewModel = hiltViewModel(),
) {
    val state = viewModel.state.collectAsStateWithLifecycle().value

    when (
        state
    ) {
        is AccountComfirmationState -> {
            GoalConfirmationScreen(
                createAccount = viewModel::initiateAccountCreation,
                userData = state.registrationInfoList,
            )
        }

        is RegisterGoalStates.GoalSelectionState -> {
            RegisterGoalsContent(
                state = state,
                onCreateAccount = viewModel::initiateAccountCreation
            )
        }

        RegisterGoalStates.AccountCreated -> {
            viewModel.returnToLoginScreen()
        }

        is RegisterGoalStates.CreationError -> {
            Box(
                modifier = Modifier
                    .background(
                        color = appColor
                    )
                    .fillMaxSize()
            ) {

            }
            ErrorDialog(
                title = "Couldn't create account!",
                error = state.message,
                onDismiss = viewModel::retry
            )
        }

        RegisterGoalStates.Loading -> {
            LoadingDialog()
        }
    }

}
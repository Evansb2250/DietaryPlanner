package com.example.googlelightcalendar.ui.screens.register.goal_creation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.googlelightcalendar.core.registration.RegisterGoalViewModel
import com.example.googlelightcalendar.core.registration.state.RegisterGoalStates.AccountComfirmationState
import com.example.googlelightcalendar.core.registration.state.RegisterGoalStates.GoalSelectionState


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
                createAccount = viewModel::createAccount,
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
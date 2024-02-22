package com.example.chooseu.ui.screens.account_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chooseu.core.AccountStates
import com.example.chooseu.core.AccountViewModel

@Composable
fun AccountScreen(
    accountViewModel: AccountViewModel = hiltViewModel()
) {
    val state = accountViewModel.state.collectAsState(initial = AccountStates.Loading).value

    AccountScreenContent(
        state = state,
        onBackPress = accountViewModel::onBackPress,
    )
}
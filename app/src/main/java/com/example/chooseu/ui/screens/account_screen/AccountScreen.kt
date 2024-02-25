package com.example.chooseu.ui.screens.account_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chooseu.core.account.state.AccountStates
import com.example.chooseu.core.account.AccountViewModel

@Composable
fun AccountScreen(
    accountViewModel: AccountViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit){
        accountViewModel.getUserInfoFromUserRepo()
    }

    val state = accountViewModel.state.collectAsState(initial = AccountStates.Loading).value

    AccountScreenContent(
        state = state,
        enableEditMode = accountViewModel::enableEditMode,
        onBackPress = accountViewModel::onBackPress,
        closeDialog = accountViewModel::cancelEditMode,
        cancel = accountViewModel::cancelEditMode,
        saveInformation = accountViewModel::saveChangesToServer
    )
}
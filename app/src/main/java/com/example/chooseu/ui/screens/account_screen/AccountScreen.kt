package com.example.chooseu.ui.screens.account_screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chooseu.core.account.AccountViewModel

@Composable
fun AccountScreen(
    accountViewModel: AccountViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit){
        accountViewModel.getUserInfoFromUserRepo()
    }

    val state = accountViewModel.state.collectAsStateWithLifecycle().value

    AccountScreenContent(
        state = state,
        enableEditMode = accountViewModel::enableEditMode,
        onBackPress = accountViewModel::onBackPress,
        closeDialog = accountViewModel::cancelEditMode,
        cancel = accountViewModel::cancelEditMode,
        showHistoryWeightHistory = accountViewModel::showWeightHistory,
        saveInformation = accountViewModel::saveChangesToServer,
        backToAccountScreen = accountViewModel::getUserInfoFromUserRepo
    )
}
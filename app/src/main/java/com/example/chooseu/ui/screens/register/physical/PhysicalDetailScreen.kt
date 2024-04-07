package com.example.chooseu.screens.register.physical

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chooseu.core.registration.PhysicalDetailsViewModel


@Composable
fun PhysicalDetailScreen(
    viewModel: PhysicalDetailsViewModel = hiltViewModel()
) {

    BackHandler {
        //clears cache and navigates to the loginScreen.
        viewModel.navigateToLoginScreen()
    }

    PhysicalDetailContent(
        state = viewModel.state.collectAsStateWithLifecycle().value,
        createAccount = viewModel::storePhysicalDetailsInCache,
        retry = viewModel::reset,
    )
}





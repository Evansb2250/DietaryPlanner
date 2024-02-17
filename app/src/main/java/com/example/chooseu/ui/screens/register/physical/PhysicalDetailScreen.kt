package com.example.chooseu.screens.register.physical

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chooseu.core.registration.PhysicalDetailsViewModel
import kotlinx.coroutines.Dispatchers


@Composable
fun PhysicalDetailScreen(
    viewModel: PhysicalDetailsViewModel = hiltViewModel()
) {

    BackHandler {
        //clears cache and navigates to the loginScreen.
        viewModel.cancelRegistration()
    }

    PhysicalDetailContent(
        state = viewModel.state.collectAsState(Dispatchers.Main.immediate).value,
        navToRegisterGoals = viewModel::storePhysicalDetailsInCache,
        retry = viewModel::reset,
    )
}





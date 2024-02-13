package com.example.googlelightcalendar.screens.register.physical

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.googlelightcalendar.core.registration.PhysicalDetailsViewModel
import kotlinx.coroutines.Dispatchers


@Composable
fun PhysicalDetailScreen(
    viewModel: PhysicalDetailsViewModel = hiltViewModel()
) {
    PhysicalDetailContent(
        state = viewModel.state.collectAsState(Dispatchers.Main.immediate).value,
        navToRegisterGoals = viewModel::storePhysicalDetailsInCache,
        retry = viewModel::reset,
    )
}





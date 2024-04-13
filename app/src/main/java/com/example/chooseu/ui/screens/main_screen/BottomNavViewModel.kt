package com.example.chooseu.ui.screens.main_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.chooseu.navigation.components.destinations.BottomNavBarDestinations
import com.example.chooseu.navigation.components.navmanagers.MainFlowNavManager
import com.example.chooseu.di.VMAssistFactoryModule
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(
    assistedFactory = VMAssistFactoryModule.BottomNavVmFactory::class,
)
class BottomNavViewModel @AssistedInject constructor(
    private val navigationManager: MainFlowNavManager,
    @Assisted private val userId: String,
) : ViewModel() {

    var isVisible by mutableStateOf(true)
        private set

    val navigationsTabs = listOf(
        BottomNavBarDestinations.Home,
        BottomNavBarDestinations.Diary,
        BottomNavBarDestinations.Calendar,
        BottomNavBarDestinations.Profile,
    )

    var selectedOption by mutableStateOf(navigationsTabs[0].routeId)
        private set

    fun navigate(
        navigationDestinations: BottomNavBarDestinations,
    ) {
        navigationManager.navigate(
            navigation = navigationDestinations,
            arguments = mapOf(
                "userId" to userId,
            )
        )
    }

    fun getNavManager(): MainFlowNavManager = navigationManager

    fun updateBottomBarTab(route: BottomNavBarDestinations? = null, currentScreen: String? = null) {
        isVisible = currentScreen in navigationsTabs.map { it.destination }
        selectedOption =
            navigationsTabs.firstOrNull { it.destination == currentScreen }?.routeId ?: 0

    }
}
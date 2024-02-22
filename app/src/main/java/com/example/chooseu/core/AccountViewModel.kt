package com.example.chooseu.core

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.domain.CurrentUser
import com.example.chooseu.navigation.components.destinations.BottomNavBarDestinations
import com.example.chooseu.navigation.components.navmanagers.AppNavManager
import com.example.chooseu.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    val appNavManager: AppNavManager,
    val userRepository: UserRepository,
) : ViewModel() {
    val state = userRepository.currentUser.map { currentUser ->
        if(currentUser != null){
            AccountStates.Success(currentUser)
        }else{
            AccountStates.Failed("information not found")
        }
    }

    fun onBackPress() {
        appNavManager.navigate(
            BottomNavBarDestinations.Profile,
        )
    }
}


sealed class AccountStates {
    object Loading: AccountStates()
    data class Success(val currentUser: CurrentUser): AccountStates()
    data class Failed(val error: String): AccountStates()
}
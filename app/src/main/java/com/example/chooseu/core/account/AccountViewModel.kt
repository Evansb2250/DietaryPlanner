package com.example.chooseu.core.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.core.account.state.AccountStates
import com.example.chooseu.domain.CurrentUser
import com.example.chooseu.navigation.components.destinations.BottomNavBarDestinations
import com.example.chooseu.navigation.components.navmanagers.AppNavManager
import com.example.chooseu.repo.UpdateResult
import com.example.chooseu.repo.UserRepository
import com.example.chooseu.utils.NumberUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val appNavManager: AppNavManager,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<AccountStates> = MutableStateFlow(AccountStates.Loading)

    val state: StateFlow<AccountStates> get() = _state


    fun getUserInfoFromUserRepo() {
        userRepository.currentUser.onEach { potentialUser ->
            //used to prevent a flicker because the compose moves between Loading to AccountInfo so fast that it can't recompose in time
            delay(30)
            _state.value = createAccountStateFromUser(potentialUser)
        }.launchIn(viewModelScope)
    }

    private fun createAccountStateFromUser(currentUser: CurrentUser?): AccountStates {
        return if (currentUser != null) {
            AccountStates.AccountInfo(currentUser = currentUser)
        } else {
            AccountStates.Error("Information not found")
        }
    }


    fun onBackPress() {
        when (state.value) {
            is AccountStates.AccountInfo -> {
                appNavManager.navigate(
                    BottomNavBarDestinations.Profile,
                )
            }
            else -> { getUserInfoFromUserRepo() }
        }
    }

    fun enableEditMode(state: AccountStates.AccountInfo) {
        _state.update {
            state.copy(readOnly = false)
        }
    }

    fun showWeightHistory() {
        viewModelScope.launch {
            _state.update {
                AccountStates.BodyMassIndexHistory(bmiHistory = userRepository.getBMIHistory())
            }
        }
    }

    fun cancelEditMode() {
        viewModelScope.launch {
            userRepository.currentUser.collect { currentUser ->
                _state.update {
                    createAccountStateFromUser(currentUser)
                }
            }
        }
    }

    fun saveChangesToServer(userInfo: AccountStates.AccountInfo) {
        setStateToLoading()

        viewModelScope.launch {
            if (userInfo.weight.isEmpty() || userInfo.height.isEmpty()) {
                setToErrorState("can't have empty space for weight or height")
            } else {
                handleUpdateUserResponse(
                    userRepository.addNewBodyMassIndexToServer(
                        weight = NumberUtils.stringToDouble(userInfo.weight),
                        weightMetric = userInfo.weightMetric,
                        height = NumberUtils.stringToDouble(userInfo.height),
                        heightMetric = userInfo.heightMetric,
                    )
                )
            }
        }
    }

    private fun setToErrorState(errorMessage: String) {
        _state.update {
            AccountStates.Error(errorMessage)
        }
    }

    private fun handleUpdateUserResponse(status: UpdateResult) {
        when (status) {
            is UpdateResult.Failed -> {
                setToErrorState(status.message)
            }

            is UpdateResult.Success -> {
                getUserInfoFromUserRepo()
            }
        }
    }

    private fun setStateToLoading() {
        _state.update {
            AccountStates.Loading
        }
    }
}
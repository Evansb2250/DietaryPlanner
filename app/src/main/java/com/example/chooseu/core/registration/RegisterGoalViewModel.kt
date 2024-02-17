package com.example.chooseu.core.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.core.registration.state.ErrorState
import com.example.chooseu.core.registration.state.RegisterGoalStates
import com.example.chooseu.core.registration.state.UnitsOfWeight
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.navmanagers.AuthNavManager
import com.example.chooseu.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterGoalViewModel @Inject constructor(
    private val navManager: AuthNavManager,
    private val userRegistrationCache: UserRegistrationCache,
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {


    private var _state: MutableStateFlow<RegisterGoalStates>
    var state: StateFlow<RegisterGoalStates>
        private set

    init {
        val userWeight = when (
            userRegistrationCache.getKey(RegistrationKeys.WEIGHTUNIT)
        ) {
            UnitsOfWeight.Kilo.type -> UnitsOfWeight.Kilo
            UnitsOfWeight.Pounds.type -> UnitsOfWeight.Pounds
            else -> {
                null
            }
        }

        _state = if (userWeight != null) {
            MutableStateFlow(
                RegisterGoalStates.GoalSelectionState(
                    initialWeight = userWeight
                )
            )
        } else {
            MutableStateFlow(
                RegisterGoalStates.GoalSelectionState(
                    initialWeight = null,
                    initialErrorState = ErrorState(
                        isError = true,
                        message = "Can't find weight entered"
                    )
                )
            )
        }

        state = _state.asStateFlow()
    }

    fun createAccount() {
        val userName = userRegistrationCache.getKey(RegistrationKeys.EMAIL) ?: ""
        val password = userRegistrationCache.getKey(RegistrationKeys.PASSWORD) ?: ""
        val firstName = userRegistrationCache.getKey(RegistrationKeys.FirstName) ?: ""
        val lastName = userRegistrationCache.getKey(RegistrationKeys.LASTNAME) ?: ""

        viewModelScope.launch(dispatcherProvider.main) {
            userRepository.createUser(
                email = userName,
                firstName = firstName,
                lastName = lastName,
                password = password,
            )
            returnToLoginScreen()
        }
    }

    private fun returnToLoginScreen() {
        //clear cache
        userRegistrationCache.clearCache()
        navManager.navigate(GeneralDestinations.OnAppStartUpDestination)
    }

    fun initiateAccountCreation(
        data: RegisterGoalStates.GoalSelectionState
    ) {
        try {
            userRegistrationCache.storeKey(
                RegistrationKeys.GoalType,
                data.selectedGoal.value.toString()
            )
            userRegistrationCache.storeKey(
                RegistrationKeys.TargetWeight,
                data.initialTargetWeight ?: ""
            )
            userRegistrationCache.storeKey(
                RegistrationKeys.AccomplishGoalByDate,
                data.dateToAccomplishGoalBy.value
            )
            userRegistrationCache.storeKey(
                RegistrationKeys.WeeklyTarget,
                data.weeklyGoalIntensity.value?.targetPerWeekInPounds.toString()
            )
            userRegistrationCache.storeKey(
                RegistrationKeys.TargetWeight,
                data.targetWeight.value.weight
            )
            userRegistrationCache.storeKey(
                RegistrationKeys.WEIGHTUNIT,
                data.targetWeight.value.weightType.type
            )

            _state.value =
                RegisterGoalStates.AccountComfirmationState(userRegistrationCache.printToList())
        } catch (e: NullPointerException) {
            Log.d("createAccount", "createAccount failed ${e.message}")
        }
    }
}



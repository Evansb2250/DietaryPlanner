package com.example.chooseu.core.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chooseu.core.dispatcher_provider.DispatcherProvider
import com.example.chooseu.core.registration.cache.UserRegistrationCache
import com.example.chooseu.core.registration.cache.keys.RegistrationKeys
import com.example.chooseu.core.registration.state.ErrorState
import com.example.chooseu.core.registration.state.RegisterGoalStates
import com.example.chooseu.core.registration.state.UnitsOfWeight
import com.example.chooseu.navigation.components.destinations.GeneralDestinations
import com.example.chooseu.navigation.components.navmanagers.AuthNavManager
import com.example.chooseu.repo.UserRepository
import com.example.chooseu.utils.AsyncResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterGoalViewModel @Inject constructor(
    private val navManager: AuthNavManager,
    private val userRegistrationCache: UserRegistrationCache,
    private val userRepository: UserRepository,
    private val dispatcherProvider: DispatcherProvider,
) : ViewModel() {


    private var _state: MutableStateFlow<RegisterGoalStates> = getGoalState()
    var state: StateFlow<RegisterGoalStates> = _state.asStateFlow()
        private set

    fun initiateAccountCreation(

    ) {
        _state.update {
            RegisterGoalStates.Loading
        }

        viewModelScope.launch(dispatcherProvider.main) {
            val response: AsyncResponse<RegisterGoalStates> =
                userRepository.createUser(userRegistrationCache.getCache())

            _state.update {
                when (response) {
                    is AsyncResponse.Failed -> response.data as RegisterGoalStates
                    is AsyncResponse.Success -> response.data!!
                }
            }
        }
    }

    fun getUserWeight(): UnitsOfWeight? {
        return when (
            userRegistrationCache.getKey(RegistrationKeys.WEIGHTUNIT)
        ) {
            UnitsOfWeight.Kilo.type -> UnitsOfWeight.Kilo
            UnitsOfWeight.Pounds.type -> UnitsOfWeight.Pounds
            else -> {
                null
            }
        }
    }

    fun getGoalState(): MutableStateFlow<RegisterGoalStates> {
        val userWeight = getUserWeight()
        return if (userWeight != null) {
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
    }

    fun retry() {
        _state.update { getGoalState().value }
    }

    fun returnToLoginScreen() {
        //clear cache
        userRegistrationCache.clearCache()
        navManager.navigate(GeneralDestinations.OnAppStartUpDestination)
    }

    fun initiateAccountCreation(
        data: RegisterGoalStates.GoalSelectionState
    ) {
        try {
            userRegistrationCache.storeKey(
                mapOf(
                    RegistrationKeys.GoalType.key to data.selectedGoal.value.toString(),
                    RegistrationKeys.TargetWeight.key to data.initialTargetWeight.toString(),
                    RegistrationKeys.AccomplishGoalByDate.key to data.dateToAccomplishGoalBy.value,
                    RegistrationKeys.WeeklyTarget.key to data.weeklyGoalIntensity.value?.targetPerWeekInPounds.toString(),
                    RegistrationKeys.TargetWeight.key to data.targetWeight.value.weight,
                    RegistrationKeys.WEIGHTUNIT.key to data.targetWeight.value.weightType.type,
                )
            )
            _state.value =
                RegisterGoalStates.AccountComfirmationState(userRegistrationCache.printToList())
        } catch (e: NullPointerException) {
            Log.d("createAccount", "createAccount failed ${e.message}")
        }
    }
}

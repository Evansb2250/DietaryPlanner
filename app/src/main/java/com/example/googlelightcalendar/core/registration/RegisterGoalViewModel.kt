package com.example.googlelightcalendar.core.registration

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.core.registration.state.ErrorState
import com.example.googlelightcalendar.core.registration.state.RegisterGoalStates
import com.example.googlelightcalendar.core.registration.state.UnitsOfWeight
import com.example.googlelightcalendar.navigation.components.NavigationDestinations
import com.example.googlelightcalendar.navigation.components.navmanagers.AuthNavManager
import com.example.googlelightcalendar.repo.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject


@HiltViewModel
class RegisterGoalViewModel @Inject constructor(
    val navManager: AuthNavManager,
    val userRegistrationCache: UserRegistrationCache,
    val userRepository: UserRepository,
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

    fun createAccount(){
//        viewModelScope.launch(Dispatchers.IO) {
//            userRepository.createUser(
//                userRegistrationCache.getKey(RegistrationKeys.EMAIL) ?: "",
//                userRegistrationCache.getKey(RegistrationKeys.FirstName) ?: "",
//                userRegistrationCache.getKey(RegistrationKeys.LASTNAME) ?: "",
//                userRegistrationCache.getKey(RegistrationKeys.PASSWORD) ?: "",
//                )
//
//        }
        returnToLoginScreen()
    }

    private fun returnToLoginScreen(){
        navManager.navigate(NavigationDestinations.OnAppStartUp)
    }
    fun initiateAccountCreation(
        data: RegisterGoalStates.GoalSelectionState
    ) {
        try{
            userRegistrationCache.storeKey(RegistrationKeys.GoalType, data.selectedGoal.value.toString())
            userRegistrationCache.storeKey(RegistrationKeys.TargetWeight, data.initialTargetWeight?:"")
            userRegistrationCache.storeKey(RegistrationKeys.AccomplishGoalByDate, data.dateToAccomplishGoalBy.value)
            userRegistrationCache.storeKey(RegistrationKeys.WeeklyTarget, data.weeklyGoalIntensity.value?.targetPerWeekInPounds.toString())
            userRegistrationCache.storeKey(RegistrationKeys.TargetWeight, data.targetWeight.value.weight)
            userRegistrationCache.storeKey(RegistrationKeys.WEIGHTUNIT, data.targetWeight.value.weightType.type)

            _state.value = RegisterGoalStates.AccountComfirmationState(userRegistrationCache.printToList())
        }catch (e: NullPointerException){
            Log.d("createAccount", "createAccount failed ${e.message}")
        }
    }
}



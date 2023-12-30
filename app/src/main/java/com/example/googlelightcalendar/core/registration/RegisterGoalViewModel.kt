package com.example.googlelightcalendar.core.registration

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.core.registration.state.ErrorState
import com.example.googlelightcalendar.core.registration.state.GoalStates
import com.example.googlelightcalendar.core.registration.state.UnitsOfWeight
import com.example.googlelightcalendar.core.registration.state.UserWeight
import com.example.googlelightcalendar.core.registration.state.WeeklyGoalIntensity
import com.example.googlelightcalendar.utils.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.lang.NumberFormatException
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject


@HiltViewModel
class RegisterGoalViewModel @Inject constructor(
    val userRegistrationCache: UserRegistrationCache,
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

}

sealed class RegisterGoalStates {
    data class GoalSelectionState(
        val initialWeight: UnitsOfWeight?,
        val initialErrorState: ErrorState = ErrorState(),
        val initialTargetWeight: String? = null,
        val accomplishGoalByDate: String? = null,
    ) : RegisterGoalStates() {

        //The previous UnitsOfWeight [kg, lb] is brought over from the cache if it turns out to be null,
        //because of a configuration change, this allows the user to set it in the goalDialog. if it is already there, they can't change the unitsOfWeight
        var missingUnitsOfWeight: Boolean = initialWeight == null

        var errorStateInGoalScreen = mutableStateOf(ErrorState())

        var errorStateInGoalDialog = mutableStateOf(ErrorState())

        //Weight to loose or to gain, it does include the total weight.
        var targetWeight = mutableStateOf(
            initialWeight.let { weightUnit ->
                UserWeight(
                    weight = "",
                    weightType = weightUnit ?: UnitsOfWeight.NotSelected,
                )
            }
        )

        //Text that appears in the drop down menu to show which [GoalIntensity] to follow each week to accomplish goal.
        val goalIntensityText = mutableStateOf("Select a goal")

        //weekly goal intensity
        val weeklyGoalIntensity = mutableStateOf<WeeklyGoalIntensity?>(null)

        //The date when to accomplish the goal
        val dateToAccomplishGoalBy = mutableStateOf("")

        val selectedGoal: MutableState<GoalStates?> = mutableStateOf(null)

        //Intensity levels for the goal selected
        val goalIntensityOptions: List<WeeklyGoalIntensity> by derivedStateOf {
            selectedGoal.value?.goalIntensityOptions?.sortedBy { it.targetPerWeekInPounds }
                ?: emptyList()
        }

        fun validateSetGoal(): Boolean {

            val futureDate = DateUtil.convertStringToDate(
                dateAsString = dateToAccomplishGoalBy.value
            )

            if (futureDate == null) {
                errorStateInGoalDialog.value = logError("No Date entered")
            } else {
                val currentDate = LocalDate.now()

                val weeksToCompleteGoal = ChronoUnit.WEEKS.between(
                    currentDate,
                    futureDate.toInstant()
                        ?.atZone(ZoneId.systemDefault())
                        ?.toLocalDate()
                )

                when (selectedGoal.value) {
                    GoalStates.TrackCalories -> {
                        when {
                            (weeksToCompleteGoal > 0) -> {
                                return true
                            }

                            (weeksToCompleteGoal == 0L) -> {
                                errorStateInGoalDialog.value =
                                    logError("You must add a date a week or more in the future")
                                return false
                            }

                            (weeksToCompleteGoal < 0) -> {
                                errorStateInGoalDialog.value =
                                    logError("You can't add a date that's already passed")
                                return false
                            }
                        }
                    }

                    else -> {
                        try {
                            // Calculate the difference between the future date and the current date
                            val totalWeightGoal = targetWeight.value.weight.toDouble()

                            val weeklyWeightGoal = weeklyGoalIntensity.value?.targetPerWeekInPounds
                                ?: throw Exception()

                            if (weeksToCompleteGoal < 0) {
                                errorStateInGoalDialog.value =
                                    logError("You can't add a date that's already passed")
                                return false
                            }

                            if (weeksToCompleteGoal >= (totalWeightGoal / weeklyWeightGoal)
                            ) {
                                return true
                            } else {
                                errorStateInGoalDialog.value =
                                    logError("You added a unrealistic goal")
                                return false
                            }

                        } catch (e: NumberFormatException) {
                            errorStateInGoalDialog.value = logError("You must add target weight!!")
                            return false

                        } catch (
                            e: Exception
                        ) {
                            errorStateInGoalDialog.value = logError("You must add target weight!!")
                            return false
                        }
                    }
                }
            }
            return false
        }

        private fun logError(
            reason: String
        ): ErrorState {
            return ErrorState(
                isError = true,
                message = reason
            )
        }

        fun getCustomDropDownIntensityTextOption(): List<String> =
            goalIntensityOptions.map { "${selectedGoal.value!!.purpose} ${it.targetPerWeekInPounds}  $initialWeight per week" }

        fun clear() {
            errorStateInGoalDialog.value.isError = false
            targetWeight.value = targetWeight.value.copy(
                weight = "",
            )
            weeklyGoalIntensity.value = null
            dateToAccomplishGoalBy.value = ""
            goalIntensityText.value = "Select a goal"
        }


        val goals = listOf(
            GoalStates.WeightLoss,
            GoalStates.WeightGain,
            GoalStates.TrackCalories
        )
    }

    object AccountComfirmationState : RegisterGoalStates()
}


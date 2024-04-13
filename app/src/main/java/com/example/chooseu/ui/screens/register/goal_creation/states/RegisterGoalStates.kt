package com.example.chooseu.ui.screens.register.goal_creation.states

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import com.example.chooseu.ui.screens.register.physical.states.ErrorState
import com.example.chooseu.ui.screens.register.physical.states.UserWeight
import com.example.chooseu.ui.screens.register.physical.states.WeightMetric
import com.example.chooseu.utils.DateUtility
import java.time.LocalDate
import java.time.temporal.ChronoUnit

sealed class RegisterGoalStates {

    object Loading : RegisterGoalStates()

    object AccountCreated: RegisterGoalStates()
    data class CreationError(val message: String) : RegisterGoalStates()
    data class AccountComfirmationState(val registrationInfoList: List<String>) :
        RegisterGoalStates() {
    }


    data class GoalSelectionState(
        val initialWeight: WeightMetric?,
        val initialErrorState: ErrorState = ErrorState(),
        val initialTargetWeight: String? = null,
        val accomplishGoalByDate: String? = null,
    ) : RegisterGoalStates() {

        //The previous UnitsOfWeight [kg, lb] is brought over from the cache if it turns out to be null,
        //because of a configuration change, this allows the user to set it in the goalDialog. if it is already there, they can't change the unitsOfWeight
        var missingUnitsOfWeight: Boolean = initialWeight == null

        var errorStateInGoalDialog = mutableStateOf(ErrorState())

        //Weight to loose or to gain, it does include the total weight.
        var targetWeight = mutableStateOf(initialWeight.let { weightUnit ->
            UserWeight(
                weight = "",
                weightType = weightUnit ?: WeightMetric.NotSelected,
            )
        })

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

            val futureDate = DateUtility.convertStringToDate(
                dateAsString = dateToAccomplishGoalBy.value
            )

            if (futureDate == null) {
                errorStateInGoalDialog.value = logError("No Date entered")
            } else {
                val currentDate = LocalDate.now()

                val weeksToCompleteGoal = ChronoUnit.WEEKS.between(
                    currentDate,
                    futureDate
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

                            if (weeksToCompleteGoal >= (totalWeightGoal / weeklyWeightGoal)) {
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
                isError = true, message = reason
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
            GoalStates.WeightLoss, GoalStates.WeightGain, GoalStates.TrackCalories
        )
    }
}

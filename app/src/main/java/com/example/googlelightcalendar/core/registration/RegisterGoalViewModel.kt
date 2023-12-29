package com.example.googlelightcalendar.core.registration

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.core.registration.Goals.TrackCalories.standarIntensityLevels
import com.example.googlelightcalendar.core.registration.state.ErrorState
import com.example.googlelightcalendar.core.registration.state.UnitsOfWeight
import com.example.googlelightcalendar.core.registration.state.UserWeight
import com.example.googlelightcalendar.utils.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import javax.inject.Inject

sealed class GoalIntensity {
    abstract val targetPerWeekInPounds: Double

    object Amature : GoalIntensity() {
        override val targetPerWeekInPounds: Double
            get() = .5

    }

    object Beginner : GoalIntensity() {
        override val targetPerWeekInPounds: Double
            get() = 1.0
    }

    object Committed : GoalIntensity() {
        override val targetPerWeekInPounds: Double
            get() = 1.5
    }

    object Ambitions : GoalIntensity() {
        override val targetPerWeekInPounds: Double
            get() = 2.0
    }
}


sealed class Goals(
    val goalPrompt: String,
    val containsWeightRequirement: Boolean,
    val imageUri: String,
    val imageDescription: String,
    val goalIntensityOptions: List<GoalIntensity>
) {
    protected val standarIntensityLevels = listOf(
        GoalIntensity.Amature,
        GoalIntensity.Beginner,
        GoalIntensity.Committed,
        GoalIntensity.Ambitions,
    )

    object WeightLoss : Goals(
        goalPrompt = "How much weight do you want to loose?",
        containsWeightRequirement = true,
        imageUri = "https://img.freepik.com/free-photo/salad-from-tomatoes-cucumber-red-onions-lettuce-leaves-healthy-summer-vitamin-menu-vegan-vegetable-food-vegetarian-dinner-table-top-view-flat-lay_2829-6482.jpg?w=1380&t=st=1702956348~exp=1702956948~hmac=9b12dccffd3c5394d1ac3f8a4c1e72eb80b319b5dd26441b291e4a593e90ee2b",
        imageDescription = "Weight loss image",
        goalIntensityOptions = standarIntensityLevels
    ) {
        override fun toString(): String {
            return "Loose"
        }
    }

    object WeightGain : Goals(
        goalPrompt = "How much weight do you want to gain?",
        containsWeightRequirement = true,
        imageUri = "https://img.freepik.com/premium-photo/table-full-food-including-burgers-fries-other-foods_873925-7494.jpg?w=1380",
        imageDescription = "Gain weight image",
        goalIntensityOptions = standarIntensityLevels,
    ) {
        override fun toString(): String {
            return "Gain"
        }
    }

    object TrackCalories : Goals(
        goalPrompt = "How long you want to track your calories?",
        containsWeightRequirement = false,
        imageUri = "https://img.freepik.com/free-photo/flay-lay-scale-weights_23-2148262188.jpg?w=1380&t=st=1702956312~exp=1702956912~hmac=bc1753ce093a63f1aa8e695185b62b6729c8216cd81d277c9a1bd44b9d1d445f",
        imageDescription = "track Calories",
        goalIntensityOptions = emptyList()
    ) {
        override fun toString(): String {
            return "Track Calories"
        }
    }
}

sealed class GoalState {
    class InProgress(
        val initialDesiredWeight: UserWeight,
        val missingUnitsOfWeight: Boolean,
    ) : GoalState() {

        var error by mutableStateOf(ErrorState())
        val initialGoalState = mutableStateOf<GoalIntensity?>(null)
        var desiredWeight = mutableStateOf(initialDesiredWeight)

        val date = mutableStateOf("")
        val goal = mutableStateOf("Select a goal")
        val selectedIntensity = mutableStateOf("Select a goal")

        fun validateDate(
            dateAsString: String
        ) {
            DateUtil.convertStringToDate(dateAsString)?.let { futureDate ->

                val currentDate = LocalDate.now()

                // Replace this with your future date

                // Calculate the difference between the future date and the current date
                val daysLeft = ChronoUnit.DAYS.between(
                    currentDate,
                    futureDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                )

                val weightGoal = desiredWeight.value.weight.toDouble()
                val weekly = initialGoalState.value?.targetPerWeekInPounds ?: 0.1
                error = if ((weightGoal / weekly) <= (daysLeft / 4)) {
                    error.copy(
                        isError = true,
                        message = "You added a realistic goal"
                    )
                } else
                    error.copy(
                        isError = true,
                        message = "You added a unrealistic goal"
                    )


            } ?: error.copy(
                isError = true,
                message = "Bad date",
            )

        }

        fun clear() {
            error.isError = false
            desiredWeight.value = initialDesiredWeight
            initialGoalState.value = null
            date.value = ""
            goal.value = "Select a goal"
            selectedIntensity.value = "Select a goal"
        }
    }
}


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

        var missingUnitsOfWeight: Boolean =  initialWeight == null
        var error = mutableStateOf(ErrorState())
        val initialGoalState = mutableStateOf<GoalIntensity?>(null)
        var desiredWeight = mutableStateOf(
            initialWeight.let { weightUnit ->
                UserWeight(
                    weight = "",
                    weightType = weightUnit ?: UnitsOfWeight.Kilo,
                )
            }
        )

        val date = mutableStateOf("")
        val goal = mutableStateOf("Select a goal")
        val selectedIntensity = mutableStateOf("Select a goal")

        fun validateDate(
            dateAsString: String
        ) {
            DateUtil.convertStringToDate(dateAsString)?.let { futureDate ->

                val currentDate = LocalDate.now()

                // Replace this with your future date

                // Calculate the difference between the future date and the current date
                val daysLeft = ChronoUnit.DAYS.between(
                    currentDate,
                    futureDate.toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDate()
                )

                val weightGoal = desiredWeight.value.weight.toDouble()
                val weekly = initialGoalState.value?.targetPerWeekInPounds ?: 0.1
                error.value = if ((weightGoal / weekly) <= (daysLeft / 4)) {
                    error.value.copy(
                        isError = true,
                        message = "You added a realistic goal"
                    )
                } else
                    error.value.copy(
                        isError = true,
                        message = "You added a unrealistic goal"
                    )


            } ?: error.value.copy(
                isError = true,
                message = "Bad date",
            )
        }

        fun clear() {
            error.value.isError = false
            desiredWeight.value = desiredWeight.value
            initialGoalState.value = null
            date.value = ""
            goal.value = "Select a goal"
            selectedIntensity.value = "Select a goal"
        }

        val goals = listOf(
            Goals.WeightLoss,
            Goals.WeightGain,
            Goals.TrackCalories
        )

        val selectedGoal: MutableState<Goals?> = mutableStateOf(null)
    }

    object AccountComfirmationState : RegisterGoalStates()
}


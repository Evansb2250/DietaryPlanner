package com.example.googlelightcalendar.core.registration

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.googlelightcalendar.navigation.components.NavigationDestinations.registerGoalsScreen
import com.example.googlelightcalendar.navigation.components.NavigationManger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

sealed class HeightUnits(val type: String) {
    data object Feet : HeightUnits("ft")
    data object Centimeter : HeightUnits("cm")
}

data class UserHeight(
    val height: String = "",
    val heightType: HeightUnits = HeightUnits.Feet,
)

data class UserWeight(
    val weight: String = "",
    val weightType: UnitsInWeight = UnitsInWeight.Kilo
)

sealed class UnitsInWeight(val type: String) {
   data object Pounds : UnitsInWeight("lb")
   data object Kilo : UnitsInWeight("kg")
}

data class ErrorHolder(
    val isError: Boolean = false,
    val message: String? = null,
)



sealed class PhysicalDetailState : RegistrationScreenStates() {
    data class PhysicalDetails(
        val errorState: ErrorHolder = ErrorHolder(),
        private val initialGenders: Genders = Genders.UNSPECIFIED,
        private val initialBirthdate: String = "",
        private val initialUserWeight: UserWeight = UserWeight(),
        private val initialUserHeight: UserHeight = UserHeight(),
    ) : PhysicalDetailState() {

        val selectedGender: MutableState<Genders> = mutableStateOf(initialGenders)
        var birthDate: MutableState<String> = mutableStateOf(initialBirthdate)
        val userHeight: MutableState<UserHeight> = mutableStateOf(initialUserHeight)
        val userWeight: MutableState<UserWeight> = mutableStateOf(initialUserWeight)

        fun completedForm(): Boolean = containsValidBirthday()
                && containsValidHeight()
                && containsValidWeight()
                && selectedAGender()

        fun containsValidBirthday(): Boolean {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            dateFormat.isLenient = false  // This will make the SimpleDateFormat strict

            return try {
                val date = dateFormat.parse(birthDate.value)
                if (date != null) {
                    val calendar = Calendar.getInstance()
                    // Subtract 18 years from the current date
                    calendar.add(Calendar.YEAR, -18)
                    val minValidDate = calendar.time
                    // Ensure the birthdate is on or before the minimum valid date (12 years ago)
                    date <= minValidDate
                } else {
                    false
                }
            } catch (e: ParseException) {
                false
            }
        }

        fun updateHeight(
            height: String
        ) {
            userHeight.value = userHeight.value.copy(height = height)

        }

        fun updateHeightMetrics(
            newMetric: String
        ) {
            val heightMetric = when (newMetric) {
                HeightUnits.Feet.type -> HeightUnits.Feet
                HeightUnits.Centimeter.type -> HeightUnits.Centimeter
                else -> HeightUnits.Feet
            }

            //reset the height
            userHeight.value = UserHeight(
                heightType = heightMetric
            )

        }


        fun updateWeight(
            weight: String
        ) {
            userWeight.value = userWeight.value.copy(weight = weight)
        }

        fun updateWeightMetrics(
            weightUnit: String
        ) {
            val updatedMetric = when (weightUnit) {
                UnitsInWeight.Kilo.type -> UnitsInWeight.Kilo
                UnitsInWeight.Pounds.type -> UnitsInWeight.Pounds
                else -> UnitsInWeight.Kilo
            }
            userWeight.value = UserWeight(
                weight = "",
                weightType = updatedMetric,
            )

        }


        fun selectedAGender(): Boolean = selectedGender.value != Genders.UNSPECIFIED

        fun containsValidHeight(): Boolean {
            return try {
                val currentHeight = userHeight.value.height.trim().toDouble()
                if (userHeight.value.heightType == HeightUnits.Feet) {
                    currentHeight > 0.0 && currentHeight <= 9.0
                } else {
                    // Convert height from inches to feet for comparison
                    val heightInFeet = currentHeight / 12.0
                    heightInFeet > 0.0 && heightInFeet <= 9.0
                }
            } catch (e: NumberFormatException) {
                false
            }
        }

        fun containsValidWeight(): Boolean {
            return try {
                val currentWeight = userWeight.value.weight.trim().toDouble()
                if (userWeight.value.weightType == UnitsInWeight.Kilo) {
                    currentWeight > 30 && currentWeight < 635
                } else {
                    currentWeight > 70 && currentWeight < 1400
                }
            } catch (
                e: NumberFormatException
            ) {
                false
            }
        }
    }

    companion object {
        val physicalDetailsTestUser = PhysicalDetails()
    }
}

@HiltViewModel
class PhysicalDetailsViewModel @Inject constructor(
    private val navigationManger: NavigationManger,
    private val cache: UserRegistrationCache,
) : ViewModel() {

    private val _state = MutableStateFlow(PhysicalDetailState.PhysicalDetails())
    val state = _state.asStateFlow()

    fun storePhysicalDetailsInCache(
        data: PhysicalDetailState.PhysicalDetails
    ) {
        if (
            data.completedForm()
        ) {
            cache.storeKey(RegistrationKeys.GENDER, data.selectedGender.value.gender)
            cache.storeKey(RegistrationKeys.BIRTHDATE, data.birthDate.value)
            cache.storeKey(RegistrationKeys.HEIGHT, data.userHeight.value.height)
            cache.storeKey(RegistrationKeys.HEIGHTUNIT, data.userHeight.value.heightType.type)
            cache.storeKey(RegistrationKeys.WEIGHT, data.userWeight.value.weight)
            cache.storeKey(RegistrationKeys.WEIGHTUNIT, data.userWeight.value.weightType.type)

            navToRegisterGoals()

            reset()
        } else {
            _state.value = PhysicalDetailState.PhysicalDetails(
                errorState = ErrorHolder(
                    isError = true,
                    message = "Missing information!! please complete form."
                )
            )
        }
    }


    private fun navToRegisterGoals() {
        navigationManger.navigate(
            registerGoalsScreen
        )
    }

    fun reset() {
        _state.value = PhysicalDetailState.PhysicalDetails()
    }

}

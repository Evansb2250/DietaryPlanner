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
import java.util.Date
import java.util.Locale
import javax.inject.Inject

sealed class HeightUnits(val unit: String) {
    object Feet : HeightUnits("ft")
    object Centermeters : HeightUnits("cm")
}

sealed class UnitsInWeight(val unit: String) {
    object Pounds : UnitsInWeight("lb")
    object Kilo : UnitsInWeight("kg")
}

sealed class PhysicalDetailState : RegistrationScreenStates() {
    data class PhysicalDetails(
        val containsError: Boolean = false,
        private val initialHeightUnit: HeightUnits = HeightUnits.Feet,
        private val initialWeightUnit: UnitsInWeight = UnitsInWeight.Pounds,
    ) : PhysicalDetailState() {

        val selectedGender: MutableState<Genders> = mutableStateOf(Genders.UNSPECIFIED)
        var birthDate: MutableState<String?> = mutableStateOf(null)

        var height: MutableState<String?> = mutableStateOf(null)
        var heightUnit: MutableState<HeightUnits> = mutableStateOf(initialHeightUnit)


        var weight: MutableState<String?> = mutableStateOf(null)
        var weightUnit: MutableState<UnitsInWeight> = mutableStateOf(initialWeightUnit)


        fun completedForm(): Boolean = containsValidBirthday()
                && containsValidHeight()
                && containsValidWeight()
                && selectedAGender()

        fun containsValidBirthday(): Boolean {
            val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
            dateFormat.isLenient = false  // This will make the SimpleDateFormat strict

            return try {
                val date = dateFormat.parse(birthDate.value ?: "")
                if (date != null) {
                    val calendar = Calendar.getInstance()
                    val today = calendar.time

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


        fun updateHeightMetrics(
            unit: String
        ) {
            heightUnit.value = when (unit) {
                HeightUnits.Feet.unit -> HeightUnits.Feet
                HeightUnits.Centermeters.unit -> HeightUnits.Centermeters
                else -> HeightUnits.Feet
            }
        }


        fun updateWeightMetrics(
            unit: String
        ) {
            weightUnit.value = when (unit) {
                UnitsInWeight.Kilo.unit -> UnitsInWeight.Kilo
                UnitsInWeight.Pounds.unit -> UnitsInWeight.Pounds
                else -> UnitsInWeight.Kilo
            }
        }

        fun selectedAGender(): Boolean = selectedGender.value != Genders.UNSPECIFIED

        fun containsValidHeight(): Boolean {
            return try {
                val currentHeight = height.value?.trim()?.let {
                    it.toDouble()
                } ?: 0.0

                if (heightUnit.value == HeightUnits.Feet) {
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
                val currentWeight = weight.value?.trim()?.toDouble() ?: 0.0
                if (weightUnit.value == UnitsInWeight.Kilo) {
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
}

@HiltViewModel
class PhysicalDetailsViewModel @Inject constructor(
    private val navigationManger: NavigationManger,
    private val cache: UserRegistrationCache,
) : ViewModel() {

    private val _state = MutableStateFlow(PhysicalDetailState.PhysicalDetails())
    val state = _state.asStateFlow()

    fun storePhysicalDetailsInCahce(
        data: PhysicalDetailState.PhysicalDetails
    ) {
        if (
            true
        ) {
//            cache.storeKey(RegistrationKeys.GENDER, data.gender.value!!.gender)
//            cache.storeKey(RegistrationKeys.BIRTHDATE, data.birthDate.value!!)
//            cache.storeKey(RegistrationKeys.HEIGHT, data.height.value!!)
//            cache.storeKey(RegistrationKeys.HEIGHTUNIT, data.heightUnit.value!!)
//            cache.storeKey(RegistrationKeys.WEIGHT, data.weight.value!!)
//            cache.storeKey(RegistrationKeys.WEIGHTUNIT, data.weightUnit.value!!)

            navToRegisterGoals()

            reset()
        } else {
            _state.value = PhysicalDetailState.PhysicalDetails(containsError = true)
        }
    }


    fun navToRegisterGoals() {
        navigationManger.navigate(
            registerGoalsScreen
        )
    }

    fun reset() {
        _state.value = PhysicalDetailState.PhysicalDetails()
    }

}


sealed class PhysicalDetailsState : RegistrationScreenStates() {
    data class UserInput(
        val gender: Genders,
        val birthDate: Date? = null,
        val height: Double,
        val weight: Double,
    ) : PhysicalDetailsState()

    data class Error(
        val errorMessage: String,
    ) : PhysicalDetailsState()

    object Success : PhysicalDetailsState()
}

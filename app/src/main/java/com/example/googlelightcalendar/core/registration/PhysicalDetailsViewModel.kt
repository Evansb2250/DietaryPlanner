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

sealed class PhysicalDetailState : RegistrationScreenStates() {
    data class PhysicalDetails(
        val containsError: Boolean = false
    ) : PhysicalDetailState() {

        var selectedGender = mutableStateOf(-1)
        var gender: MutableState<Genders?> = mutableStateOf(null)
        var birthDate: MutableState<String?> = mutableStateOf(null)
        var height: MutableState<String?> = mutableStateOf(null)
        var heightUnit: MutableState<String?> = mutableStateOf("ft")
        var weight: MutableState<String?> = mutableStateOf(null)
        var weightUnit: MutableState<String?> = mutableStateOf("kg")


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

                    // Subtract 12 years from the current date
                    calendar.add(Calendar.YEAR, -12)
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

        fun selectedAGender(): Boolean = gender.value != null

        fun containsValidHeight(): Boolean {
            return try {
                val currentHeight = height.value?.trim()?.toDouble() ?: 0.0
                if (heightUnit.value == "ft") {
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
                if (weightUnit.value == "kg") {
                    currentWeight > 0.0 && currentWeight < 635
                } else {
                    currentWeight > 0 && currentWeight < 1400
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
            data.completedForm()
        ) {
            cache.storeKey(RegistrationKeys.GENDER, data.gender.value!!.gender)
            cache.storeKey(RegistrationKeys.BIRTHDATE, data.birthDate.value!!)
            cache.storeKey(RegistrationKeys.HEIGHT, data.height.value!!)
            cache.storeKey(RegistrationKeys.HEIGHTUNIT, data.heightUnit.value!!)
            cache.storeKey(RegistrationKeys.WEIGHT, data.weight.value!!)
            cache.storeKey(RegistrationKeys.WEIGHTUNIT, data.weightUnit.value!!)

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

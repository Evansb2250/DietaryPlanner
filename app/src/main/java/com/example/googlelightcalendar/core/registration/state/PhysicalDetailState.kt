package com.example.googlelightcalendar.core.registration.state

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.googlelightcalendar.core.registration.Genders
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

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



sealed class PhysicalDetailState {
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
}
package com.example.chooseu.ui.screens.account_screen.state

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.chooseu.ui.screens.register.physical.states.HeightMetric
import com.example.chooseu.ui.screens.register.physical.states.WeightMetric
import com.example.chooseu.domain.BodyMassIndex
import com.example.chooseu.domain.CurrentUser
import com.example.chooseu.utils.NumberUtils
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class AccountStates(
    open val title: String = "Account Info"
) {
    object Loading : AccountStates()
    data class AccountInfo(
        val readOnly: Boolean = true,
        val currentUser: CurrentUser,
    ) :
        AccountStates() {

        var heightMetric by mutableStateOf(currentUser.heightMetric)
            private set

        var weightMetric by mutableStateOf(currentUser.weightMetric)
            private set

        var height by mutableStateOf(currentUser.height.toString())
            private set
        var weight by mutableStateOf(currentUser.weight.toString())
            private set

        fun updateHeight(heightInString: String) {
            height = NumberUtils.updateStringToValidNumber(heightInString)
        }

        fun updateWeight(weightInString: String) {
            weight = NumberUtils.updateStringToValidNumber(weightInString)
        }

        fun updateHeightMetric(newHeightMetric: String) {
            if (heightMetric != newHeightMetric) {
                val conversionFunction: (Double) -> Double = when (newHeightMetric) {
                    HeightMetric.Feet.type -> {
                        NumberUtils::convertCentimetersToFeet
                    }
                    else -> {
                        NumberUtils::convertFeetToCentimeters
                    }
                }
                height = formatDoubleToString(conversionFunction(NumberUtils.stringToDouble(height)))
                heightMetric = newHeightMetric
            }
        }

        fun updateWeightMetric(newWeightMetric: String) {
            if (weightMetric != newWeightMetric) {
                val conversionFunction: (Double) -> Double = when (newWeightMetric) {
                    WeightMetric.Kilo.type -> NumberUtils::convertPoundsToKG
                    else -> NumberUtils::convertKGToPounds
                }

                weight = formatDoubleToString(conversionFunction(NumberUtils.stringToDouble(weight)))
                weightMetric = newWeightMetric
            }
        }

        fun calculateAge(): Int {
            val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")
            val birthLocalDate = LocalDate.parse(currentUser.birthdate, formatter)
            val currentLocalDate = LocalDate.now()

            // Calculate age
            var age = currentLocalDate.year - birthLocalDate.year

            // Adjust age if the birthdate hasn't occurred yet this year
            if (currentLocalDate < birthLocalDate.plusYears(age.toLong())) {
                age--
            }

            return age
        }

    }

    fun formatDoubleToString(number: Double): String {
        val decimalFormat = DecimalFormat("#.#")
        return decimalFormat.format(number)
    }


    data class BodyMassIndexHistory(
        override val title: String = "Weight History",
        private val bmiHistory: List<BodyMassIndex>
    ) : AccountStates() {
        var weightHistory by mutableStateOf(bmiHistory)
            private set
    }

    data class Error(val error: String) : AccountStates()
}
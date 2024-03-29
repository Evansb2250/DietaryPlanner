package com.example.chooseu.utils

private const val kgToPound = 2.2
private const val feetPerInches = 12

object NumberUtils {

    fun convertPoundsToKG(amountInPounds: Double): Double{
        return amountInPounds / kgToPound
    }

    fun convertKGToPounds(amountInKG: Double): Double{
        return amountInKG *  kgToPound
    }

    fun convertFeetToCentimeters(feets: Double): Double{
       return feets * feetPerInches
    }

    fun convertCentimetersToFeet(feets: Double): Double{
        return feets / feetPerInches
    }





    fun updateStringToValidNumber(heightInString: String): String {
        return try {
            if (heightInString.trim().toDouble() <= 0) {
                ""
            } else {
                heightInString
            }
        } catch (e: Exception) {
            ""
        }
    }

    fun stringToDouble(stringNumber: String): Double {
        return try {
            if (stringNumber.trim().toDouble() <= 1.0) {
                1.0
            }
            stringNumber.trim().toDouble()
        } catch (e: Exception) {
            1.0
        }
    }
}
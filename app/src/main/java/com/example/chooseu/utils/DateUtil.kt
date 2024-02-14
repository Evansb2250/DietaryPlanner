package com.example.chooseu.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

object DateUtil {
    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    fun convertStringToDate(
        dateAsString: String,
    ): Date? {

        dateFormat.isLenient = false  // This will make the SimpleDateFormat strict
        return try {
            val date = dateFormat.parse(dateAsString)
            return date
        } catch (e: ParseException) {
            null
        }
    }


    fun convertDateToString(date: LocalDate): String {
        var formattedDate: String
        try {
            val dateObject: Date = Date.from(date.atStartOfDay(ZoneId.of("America/Bogota")).toInstant())
            formattedDate = dateFormat.format(dateObject)
        } catch (e: Exception) {
            // Handle the exception if needed
            e.printStackTrace()
            formattedDate = ""
        }
        return formattedDate
    }
}

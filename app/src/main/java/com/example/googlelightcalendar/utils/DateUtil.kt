package com.example.googlelightcalendar.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

object DateUtil {
    fun convertStringToDate(
        dateAsString: String,
    ): Date? {
        val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
        dateFormat.isLenient = false  // This will make the SimpleDateFormat strict
        return try {
            val date = dateFormat.parse(dateAsString)
            return date
        } catch (e: ParseException) {
            null
        }
    }


    fun convertDateToString(date: Date): String {
        return ""
    }
}

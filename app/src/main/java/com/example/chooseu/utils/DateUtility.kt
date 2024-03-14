package com.example.chooseu.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateUtility {
    private val dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy")
    private val zone = ZoneId.of("America/Bogota")
    fun convertStringToDate(dateAsString: String): LocalDate? {
        return try {
            LocalDate.parse(dateAsString, dateFormat)
        } catch (e: Exception) {
            null
        }
    }

    fun convertStringStringDateToLong(dateAsString: String): Long {
        val localDate = LocalDate.parse(dateAsString, dateFormat)
        return localDate.atStartOfDay(zone).toInstant().toEpochMilli()
    }

    fun calculateDateAsLong(date: LocalDate? = null): Long {
        val currentDate = date ?: LocalDate.now()
        return currentDate.atStartOfDay(zone).toInstant().toEpochMilli()
    }

    fun convertLocalDateToString(date: LocalDate): String {
        return date.format(dateFormat)
    }

    fun convertDateLongToLocalDate(date: Long): LocalDate {
        return Instant.ofEpochMilli(date)
            .atZone(zone)
            .toLocalDate()
    }

    fun convertDateLongToString(date: Long): String {
        val instant = Instant.ofEpochMilli(date)
        val localDate = instant.atZone(zone).toLocalDate()
        return localDate.format(dateFormat)
    }

    fun getPreviousDay(date: LocalDate): LocalDate {
        return date.minusDays(1)
    }

    fun getNextDayAsDate(date: LocalDate): LocalDate {
        return date.plusDays(1)
    }
}



package com.example.chooseu.core.diary.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.chooseu.core.diary.models.Breakfast
import com.example.chooseu.core.diary.models.Dinner
import com.example.chooseu.core.diary.models.Lunch
import com.example.chooseu.utils.DateUtility
import java.time.LocalDate

sealed class DiaryScreenStates {
    data class FoodDiaryEntry(
        private val localDate: LocalDate = LocalDate.now(),
        val breakfast: Breakfast = Breakfast(),
        val lunch: Lunch = Lunch(),
        val dinner: Dinner = Dinner(),
    ): DiaryScreenStates(){
        var date by mutableStateOf(
            DateUtility.convertLocalDateToString(localDate)
        )
    }
}

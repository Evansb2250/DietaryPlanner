package com.example.googlelightcalendar.core.diary.states

import com.example.googlelightcalendar.core.diary.models.Breakfast
import com.example.googlelightcalendar.core.diary.models.Dinner
import com.example.googlelightcalendar.core.diary.models.Lunch
import java.time.LocalDate

sealed class DiaryScreenStates() {
    data class MealDiary(
        val date: LocalDate,
        val breakfast: Breakfast = Breakfast(),
        val lunch: Lunch = Lunch(),
        val dinner: Dinner = Dinner(),
    ) : DiaryScreenStates()
}

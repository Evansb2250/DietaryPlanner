package com.example.chooseu.core.diary.states

import com.example.chooseu.core.diary.models.Breakfast
import com.example.chooseu.core.diary.models.Dinner
import com.example.chooseu.core.diary.models.Lunch
import java.time.LocalDate

sealed class DiaryScreenStates() {
    data class MealDiary(
        val date: LocalDate,
        val breakfast: Breakfast = Breakfast(),
        val lunch: Lunch = Lunch(),
        val dinner: Dinner = Dinner(),
    ) : DiaryScreenStates()
}

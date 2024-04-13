package com.example.chooseu.ui.screens.register.goal_creation.states

sealed class WeeklyGoalIntensity {
    abstract val targetPerWeekInPounds: Double

    object Amature : WeeklyGoalIntensity() {
        override val targetPerWeekInPounds: Double
            get() = .5

    }

    object Beginner : WeeklyGoalIntensity() {
        override val targetPerWeekInPounds: Double
            get() = 1.0
    }

    object Committed : WeeklyGoalIntensity() {
        override val targetPerWeekInPounds: Double
            get() = 1.5
    }

    object Ambitions : WeeklyGoalIntensity() {
        override val targetPerWeekInPounds: Double
            get() = 2.0
    }
}

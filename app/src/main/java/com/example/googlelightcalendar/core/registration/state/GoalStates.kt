package com.example.googlelightcalendar.core.registration.state

import com.example.googlelightcalendar.core.registration.state.GoalStates.TrackCalories.purpose
import com.example.googlelightcalendar.core.registration.state.GoalStates.TrackCalories.standarIntensityLevels

sealed class GoalStates(
    val goalPrompt: String,
    val purpose: String,
    val containsWeightRequirement: Boolean,
    val imageUri: String,
    val imageDescription: String,
    val goalIntensityOptions: List<WeeklyGoalIntensity>
) {
    protected val standarIntensityLevels = listOf(
        WeeklyGoalIntensity.Amature,
        WeeklyGoalIntensity.Beginner,
        WeeklyGoalIntensity.Committed,
        WeeklyGoalIntensity.Ambitions,
    )

    object WeightLoss : GoalStates(
        purpose = "Loose",
        goalPrompt = "How much weight do you want to loose?",
        containsWeightRequirement = true,
        imageUri = "https://img.freepik.com/free-photo/salad-from-tomatoes-cucumber-red-onions-lettuce-leaves-healthy-summer-vitamin-menu-vegan-vegetable-food-vegetarian-dinner-table-top-view-flat-lay_2829-6482.jpg?w=1380&t=st=1702956348~exp=1702956948~hmac=9b12dccffd3c5394d1ac3f8a4c1e72eb80b319b5dd26441b291e4a593e90ee2b",
        imageDescription = "Weight loss image",
        goalIntensityOptions = standarIntensityLevels
    ) {
        override fun toString(): String {
            return "Loose Weight"
        }
    }

    object WeightGain : GoalStates(
        purpose = "Gain",
        goalPrompt = "How much weight do you want to gain?",
        containsWeightRequirement = true,
        imageUri = "https://img.freepik.com/premium-photo/table-full-food-including-burgers-fries-other-foods_873925-7494.jpg?w=1380",
        imageDescription = "Gain weight image",
        goalIntensityOptions = standarIntensityLevels,
    ) {
        override fun toString(): String {
            return "Gain Weight"
        }
    }

    object TrackCalories : GoalStates(
        purpose = "Track",
        goalPrompt = "How long you want to track your calories?",
        containsWeightRequirement = false,
        imageUri = "https://img.freepik.com/free-photo/flay-lay-scale-weights_23-2148262188.jpg?w=1380&t=st=1702956312~exp=1702956912~hmac=bc1753ce093a63f1aa8e695185b62b6729c8216cd81d277c9a1bd44b9d1d445f",
        imageDescription = "track Calories",
        goalIntensityOptions = emptyList()
    ) {
        override fun toString(): String {
            return "Track Calories"
        }
    }
}

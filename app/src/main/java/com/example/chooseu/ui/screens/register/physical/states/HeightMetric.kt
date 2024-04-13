package com.example.chooseu.ui.screens.register.physical.states

sealed class HeightMetric(val type: String) {
    data object Feet : HeightMetric("ft")
    data object Inches : HeightMetric("in")
}
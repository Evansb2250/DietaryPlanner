package com.example.chooseu.ui.screens.register.physical.states

sealed class WeightMetric(val type: String) {
    data object Pounds : WeightMetric("lb")
    data object Kilo : WeightMetric("kg")

    data object NotSelected : WeightMetric("")
}
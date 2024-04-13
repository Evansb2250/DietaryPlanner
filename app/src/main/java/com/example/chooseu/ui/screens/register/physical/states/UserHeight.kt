package com.example.chooseu.ui.screens.register.physical.states

data class UserHeight(
    val height: String = "",
    val heightType: HeightMetric = HeightMetric.Feet,
)
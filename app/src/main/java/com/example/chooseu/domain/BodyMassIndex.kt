package com.example.chooseu.domain

//Each document Id has a unique date.
data class BodyMassIndex(
    val userId: String,
    val weight: Double,
    val weightMetric: String,
    val height: Double,
    val heightMetric: String,
    val bmi: Double = 0.0,
    val date: String,
)
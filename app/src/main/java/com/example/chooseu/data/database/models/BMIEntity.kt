package com.example.chooseu.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class BMIEntity(
    @PrimaryKey
    val documentId: String,
    val userId: String,
    val weight: Double,
    val weightMetric: String,
    val height: Double,
    val heightMetric: String,
    val bmi: Double = 0.0,
    val dateAsInteger: Long,
)
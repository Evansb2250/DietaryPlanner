package com.example.chooseu.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.chooseu.domain.BodyMassIndex
import com.example.chooseu.utils.DateUtil

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

fun BMIEntity.toBodyMassIndex(): BodyMassIndex = BodyMassIndex(
    userId = this.userId,
    weight = this.weight,
    weightMetric = this.weightMetric,
    height = this.height,
    heightMetric = this.heightMetric,
    bmi = this.bmi,
    date = DateUtil.convertDateLongToString(this.dateAsInteger)
)

fun List<BMIEntity>.toBodyMassIndexList(): List<BodyMassIndex> =
    this.sortedByDescending { it.dateAsInteger }.map {
        it.toBodyMassIndex()
    }

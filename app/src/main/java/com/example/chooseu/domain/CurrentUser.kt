package com.example.chooseu.domain

import com.example.chooseu.data.database.models.UserEntity

data class CurrentUser(
    val userName: String,
    val name: String,
    val lastName: String = "",
    val gender: String = "",
    val email: String = "",
    val birthdate: String = "",
    val heightMetric: String = "",
    val height: Double = 0.0,
    val weightMetric: String = "",
    val weight: Double = 0.0,
)


fun CurrentUser.toUserEntity(): UserEntity {
    return UserEntity(
        userName = this.userName,
        password = "",
        name = this.name,
        lastName = ""
    )
}
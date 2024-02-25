package com.example.chooseu.data.database.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.chooseu.domain.CurrentUser

@Entity
data class UserEntity(
    @PrimaryKey
    val userName: String,
    val name: String,
    val lastName: String,
    val password: String
)

//TODO("I need to delete this won't be storing user data in room")
fun UserEntity.toUser(): CurrentUser = CurrentUser(
    userName = this.userName,
    name = this.name,
    lastName = "",
    gender = "",
    email = "",
    birthdate = "",
    heightMetric = "",
    height = 0.0,
    weightMetric = "",
    weight = 0.0,
)
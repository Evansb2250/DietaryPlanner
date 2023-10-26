package com.example.googlelightcalendar.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class UserEntity(
    @PrimaryKey
    val userName:String,
    val name: String,
    val password: String
)

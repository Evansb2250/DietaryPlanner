package com.example.googlelightcalendar.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {
    @Insert
    suspend fun insertUser(newUser: UserDao)

    @Query("Select * From USERENTITY where userName=:emailName AND password=:password  ")
    suspend fun getUser(
        emailName: String,
        password: String,
    ): UserDao

    @Delete
    suspend fun deleteUser(
        userDao: UserDao
    )
}
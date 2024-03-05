package com.example.chooseu.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.chooseu.data.database.models.BMIEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BMIDao {
    @Insert
    suspend fun insertBodyMassIndex(
        bmiEntity: BMIEntity
    )

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertListOfBMI(
        listOfBMI: List<BMIEntity>
    )

    @Query("Select * From BMIEntity where userId =:id")
    fun getBMIHistory(id: String): List<BMIEntity>


    @Query("SELECT * FROM BMIEntity ORDER BY dateAsInteger DESC LIMIT 1")
    fun getCurrentBMI(): Flow<BMIEntity>


    @Query("SELECT * FROM BMIEntity Where dateAsInteger=:date")
    suspend fun dateAlreadyExist(date: Long): BMIEntity?

    @Query("DELETE FROM BMIEntity")
    suspend fun deleteAll()
}
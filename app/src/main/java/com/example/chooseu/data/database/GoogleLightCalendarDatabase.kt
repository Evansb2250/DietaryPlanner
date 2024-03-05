package com.example.chooseu.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.chooseu.data.database.dao.BMIDao
import com.example.chooseu.data.database.dao.UserDao
import com.example.chooseu.data.database.models.BMIEntity
import com.example.chooseu.data.database.models.UserEntity

@Database(
    entities = arrayOf(UserEntity::class, BMIEntity::class),
    version = 4
)
abstract class GoogleLightCalenderDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun bodyMassIndexDao(): BMIDao

    companion object {
        private lateinit var instance: GoogleLightCalenderDatabase
        fun getDatabase(
            context: Context,
        ): GoogleLightCalenderDatabase {
            if (!Companion::instance.isInitialized) {
                val database = Room.databaseBuilder(
                    context,
                    GoogleLightCalenderDatabase::class.java,
                    "GoogleLightCalendarDatabase"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                instance = database
            }
            return instance
        }
    }
}
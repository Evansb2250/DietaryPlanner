package com.example.googlelightcalendar.data.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.googlelightcalendar.data.room.database.dao.UserDao
import com.example.googlelightcalendar.data.room.database.models.UserEntity

@Database(
    entities = arrayOf(UserEntity::class),
    version = 1
)
abstract class GoogleLightCalenderDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object{
        private lateinit var instance: GoogleLightCalenderDatabase
        fun getDatabase(
            context: Context,
        ): GoogleLightCalenderDatabase {
            if(!::instance.isInitialized){
                val database = Room.databaseBuilder(
                    context,
                    GoogleLightCalenderDatabase::class.java,
                    "GoogleLightCalendarDatabase"
                ).build()

                instance = database
            }
            return instance
        }
    }
}
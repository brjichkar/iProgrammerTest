package com.iprogrammertest.db_section

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.iprogrammertest.ui_section.city_weather_section.model.Main

@Database(entities = [Main::class], version = 1 , exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase()  {
    abstract fun weatherDao(): WeatherDAO

    companion object {
        private var INSTANCE: AppDatabase? = null
        @Synchronized fun  getDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                synchronized(AppDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "WeatherInfo")
                        .build()
                }
            }
            return INSTANCE
        }

    }
}
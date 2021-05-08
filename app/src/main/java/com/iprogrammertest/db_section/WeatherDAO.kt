package com.iprogrammertest.db_section

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.iprogrammertest.ui_section.city_weather_section.model.Main
import java.util.*

@Dao
interface WeatherDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOneRecord(weatherInfo: Main)

    @get:Query("select * from main")
    val allCities: List<Main>

    @Query("select * from main WHERE cityName like :cityIn and createdTime >= :timeLessthan10Mins ORDER BY createdTime desc")
    fun getCitiesForAutoPopulate(cityIn: String?,timeLessthan10Mins: Date?): List<Main?>?

    @Query("SELECT  * FROM Main ORDER BY createdTime desc")
    fun getCities(): List<Main>
}
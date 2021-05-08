package com.iprogrammertest.ui_section.city_weather_section.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Main(
    var temp: Double,
    var temp_max: Double,
    var temp_min: Double,
    var storeTime:String,
    var createdTime: Date,
    @PrimaryKey
    var cityName:String
)
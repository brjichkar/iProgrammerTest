
package com.iprogrammertest.api_section

import com.iprogrammertest.ui_section.city_weather_section.model.WeatherResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ApiInterface {

    //Sample URL - http://api.openweathermap.org/data/2.5/weather?q=Pune&appid=094aa776d64c50d5b9e9043edd4ffd00

    /**
     *  @Function : getCurrentWeatherFromCityName()
     *  @params   : @Query("q") q:String, @Query("appid") appid:String
     *  @Return   : WeatherResponse
     * 	@Usage	  : request to get current weather of specific city
     */
    @GET("weather")
    fun getCurrentWeatherFromCityName(@Query("q") q:String, @Query("appid") appid:String,@Query("units") units:String): Call<WeatherResponse>

}



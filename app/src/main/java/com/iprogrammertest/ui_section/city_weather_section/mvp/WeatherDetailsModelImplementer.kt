package com.iprogrammertest.ui_section.city_weather_section.mvp

import android.os.AsyncTask
import android.util.Log
import com.iprogrammertest.api_section.ApiClient
import com.iprogrammertest.api_section.ApiInterface
import com.iprogrammertest.db_section.AppDatabase
import com.iprogrammertest.ui_section.city_weather_section.model.Main
import com.iprogrammertest.ui_section.city_weather_section.model.WeatherResponse
import com.iprogrammertest.utility_section.CommonUtils
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class WeatherDetailsModelImplementer: WeatherDetailsMVP.WeatherDetailsModel {
    private var mApiInterfaceService: ApiInterface = ApiClient().getClient().create(ApiInterface::class.java)

    override fun processWeatherDataRequest(
        cityName: String,
        appId: String,
        onFinishListener: WeatherDetailsMVP.WeatherDetailsModel.OnWeatherRequestFinish,
        appDb:AppDatabase
    ) {
        try {

            val call = mApiInterfaceService.getCurrentWeatherFromCityName(cityName,appId,"metric")
            call.enqueue(object: Callback<WeatherResponse> {
                override fun onResponse(call: Call<WeatherResponse>, response: Response<WeatherResponse>) {
                    if (response.code() == 200) {
                        val storeEntry=response.body()!!.main
                        storeEntry.cityName=response.body()!!.name
                        storeEntry.storeTime=CommonUtils.getCurrentTimestampForUse()
                        storeEntry.createdTime= Date(System.currentTimeMillis())
                        SaveWeatherData(appDb,onFinishListener,storeEntry).execute()
                    } else {
                        if (response.errorBody() != null) {
                            val jObjError = JSONObject(response.errorBody()!!.string())
                            onFinishListener.onRequestError(jObjError.get("message") as String)
                        } else {
                            onFinishListener.onRequestError(response.errorBody()!!.string())
                        }
                    }
                }
                override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                    onFinishListener.onRequestError(t.message!!)
                }
            })
        } catch (e: Exception) {
            onFinishListener.onRequestError("")
        }
    }

    override fun getAllCityListStoredInDB(
        onGetRecordFinishListener: WeatherDetailsMVP.WeatherDetailsModel.OnGetRecordFinish,
        appDb: AppDatabase
    ) {
        try {
            GetWeatherData(appDb,onGetRecordFinishListener).execute()
        }
        catch (ex:Exception){
            onGetRecordFinishListener.onGetRequestError("")
        }
    }

    private class SaveWeatherData(
        var appDatabase: AppDatabase,
        var onFinishListener: WeatherDetailsMVP.WeatherDetailsModel.OnWeatherRequestFinish,
        var storeEntry: Main
    ) : AsyncTask<Void, Void, Int>() {
        override fun doInBackground(vararg params: Void?): Int {
            appDatabase.weatherDao().insertOneRecord(storeEntry)
            return 1
        }
        override fun onPostExecute(tempList: Int ?) {
            onFinishListener.onRequestFinished()
        }
    }

    private class GetWeatherData(
        var appDatabase: AppDatabase,
        var onGetRecordFinishListener: WeatherDetailsMVP.WeatherDetailsModel.OnGetRecordFinish
    ) : AsyncTask<Void, Void, List<Main>>() {
        override fun doInBackground(vararg params: Void?): List<Main> {
            val tempList= appDatabase.weatherDao().getCities()
            return tempList
        }
        override fun onPostExecute(tempList: List<Main> ?) {
            if (tempList!!.isNotEmpty()) {
                onGetRecordFinishListener.onGetRecordFinish(tempList)
            } else {
                onGetRecordFinishListener.onGetRequestError("No Record found")
            }
        }
    }
}
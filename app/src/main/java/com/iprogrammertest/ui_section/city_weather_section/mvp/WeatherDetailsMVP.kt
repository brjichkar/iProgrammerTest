package com.iprogrammertest.ui_section.city_weather_section.mvp

import com.iprogrammertest.db_section.AppDatabase
import com.iprogrammertest.ui_section.base_class_section.MvpView
import com.iprogrammertest.ui_section.city_weather_section.model.Main
import com.iprogrammertest.ui_section.city_weather_section.model.WeatherResponse

class WeatherDetailsMVP {
    interface WeatherDetailsView:MvpView{
        fun onWeatherDetailsSuccessful()
        fun onWeatherDetailsFailed(errorMsg: String)
        fun getAllCityRecord(tempList:List<Main>)
    }
    interface WeatherDetailsPresenter{
        fun attachView(weatherView: WeatherDetailsView)
        fun destroyView()
        fun onRatingDataRequested(cityName: String, appId:String,appDb:AppDatabase)
        fun onGetAllCityRequest(appDb:AppDatabase)
    }
    interface WeatherDetailsModel{
        fun processWeatherDataRequest(cityName: String, appId:String,onFinishListener:OnWeatherRequestFinish,appDb: AppDatabase)
        interface OnWeatherRequestFinish{
            fun onRequestFinished()
            fun onRequestError(errorMsg: String)
        }

        fun getAllCityListStoredInDB(onGetRecordFinishListener:OnGetRecordFinish,appDb: AppDatabase)
        interface OnGetRecordFinish{
            fun onGetRecordFinish(tempList:List<Main>)
            fun onGetRequestError(errorMsg: String)
        }
    }
}
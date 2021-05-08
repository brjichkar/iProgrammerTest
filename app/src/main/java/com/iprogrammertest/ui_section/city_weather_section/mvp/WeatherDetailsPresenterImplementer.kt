package com.iprogrammertest.ui_section.city_weather_section.mvp

import com.iprogrammertest.R
import com.iprogrammertest.db_section.AppDatabase
import com.iprogrammertest.ui_section.city_weather_section.model.Main
import com.iprogrammertest.ui_section.city_weather_section.model.WeatherResponse

class WeatherDetailsPresenterImplementer(mview:WeatherDetailsMVP.WeatherDetailsView):
    WeatherDetailsMVP.WeatherDetailsPresenter,
    WeatherDetailsMVP.WeatherDetailsModel.OnWeatherRequestFinish,
    WeatherDetailsMVP.WeatherDetailsModel.OnGetRecordFinish
{

    private var mView: WeatherDetailsMVP.WeatherDetailsView?=mview
    private var mModel=WeatherDetailsModelImplementer()

    override fun attachView(weatherView: WeatherDetailsMVP.WeatherDetailsView) {
        mView=weatherView
    }

    override fun destroyView() {
        mView=null
    }

    override fun onRatingDataRequested(cityName: String, appId: String,appDb: AppDatabase) {
        if(mView!=null){
            if(mView!!.isNetworkConnected){
                mView!!.showLoading()
                mModel.processWeatherDataRequest(cityName,appId,this,appDb)
            }
            else{
                mView!!.hideLoading()
                mView!!.onError(R.string.not_connected_to_internet)
            }
        }
    }

    override fun onGetAllCityRequest(appDb: AppDatabase) {
        if(mView!=null){
            mView!!.showLoading()
            mModel.getAllCityListStoredInDB(this,appDb)
        }
    }

    override fun onRequestFinished() {
        if (mView != null) {
            mView!!.hideLoading()
            mView!!.onWeatherDetailsSuccessful()
        }
    }

    override fun onRequestError(errorMsg: String) {
        if (mView != null) {
            mView!!.hideLoading()
            if(errorMsg == ""){
                mView!!.onError(R.string.some_error)
            }else{
                mView!!.onWeatherDetailsFailed(errorMsg)
            }
        }
    }

    override fun onGetRecordFinish(tempList: List<Main>) {
        if (mView != null) {
            mView!!.hideLoading()
            mView!!.getAllCityRecord(tempList)
        }
    }

    override fun onGetRequestError(errorMsg: String) {
        if (mView != null) {
            mView!!.hideLoading()
            mView!!.onError(errorMsg)
        }
    }
}
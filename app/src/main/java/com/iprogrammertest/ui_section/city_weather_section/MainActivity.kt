package com.iprogrammertest.ui_section.city_weather_section

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView.OnItemClickListener
import android.widget.AutoCompleteTextView
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AlertDialog
import com.iprogrammertest.R
import com.iprogrammertest.db_section.AppDatabase
import com.iprogrammertest.ui_section.base_class_section.BaseActivity
import com.iprogrammertest.ui_section.city_weather_section.adapter.AdapterCityRecords
import com.iprogrammertest.ui_section.city_weather_section.adapter.CityListAdapter
import com.iprogrammertest.ui_section.city_weather_section.decorator.SimpleDividerItemDecoration
import com.iprogrammertest.ui_section.city_weather_section.model.Main
import com.iprogrammertest.ui_section.city_weather_section.mvp.WeatherDetailsMVP
import com.iprogrammertest.ui_section.city_weather_section.mvp.WeatherDetailsPresenterImplementer
import java.util.*


class MainActivity : BaseActivity(),WeatherDetailsMVP.WeatherDetailsView {
    private lateinit var mPresenter: WeatherDetailsMVP.WeatherDetailsPresenter
    private lateinit var mAppDb:AppDatabase
    private var mPreviousSearchList: MutableList<Main> = mutableListOf()
    private lateinit var mAdapter: AdapterCityRecords
    private lateinit var mRvInfoList: androidx.recyclerview.widget.RecyclerView
    private lateinit var mCityAutocomplete:AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setData()
        mPresenter.onGetAllCityRequest(mAppDb)
    }
    override fun onStart() {
        super.onStart()
    }
    override fun onResume() {
        super.onResume()
        mPresenter.attachView(this)
    }

    override fun onStop() {
        super.onStop()
        mPresenter.destroyView()
    }

    private fun setData(){
        mAppDb= AppDatabase.getDatabase(this)!!
        mPresenter = WeatherDetailsPresenterImplementer(this)
        mRvInfoList=findViewById(R.id.act_main_rv_info)
        mAdapter= AdapterCityRecords(mPreviousSearchList)
        val mLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mRvInfoList.layoutManager = mLayoutManager
        mRvInfoList.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        mRvInfoList.addItemDecoration(SimpleDividerItemDecoration(resources.getDrawable(R.drawable.points_separator)))
        mRvInfoList.adapter = mAdapter
        mCityAutocomplete = findViewById(R.id.auto_city)
        val storeOffers: List<Main> = ArrayList<Main>()
        val adapter = CityListAdapter(this, R.layout.row_city_item, storeOffers,mAppDb)
        mCityAutocomplete.setAdapter(adapter)
        mCityAutocomplete.onItemClickListener = onItemClickListener
        mCityAutocomplete.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                hideKeyboard()
                mPresenter.onRatingDataRequested(mCityAutocomplete.text.toString().trim(),"094aa776d64c50d5b9e9043edd4ffd00",mAppDb)
            }
            true
        })
    }

    // api call of getting weather details successful.
    override fun onWeatherDetailsSuccessful() {
        onError("Record Added")
        mCityAutocomplete.setText("")
        mPresenter.onGetAllCityRequest(mAppDb)
    }

    // api call of getting weather details failed.
    override fun onWeatherDetailsFailed(errorMsg: String) {
        showAlertForError(errorMsg)
    }

    // api call of getting all city details present from DB
    override fun getAllCityRecord(tempList: List<Main>) {
        mPreviousSearchList.clear()
        mPreviousSearchList.addAll(tempList)
        mAdapter.notifyDataSetChanged()
    }

     private val onItemClickListener = OnItemClickListener { adapterView, view, i, l ->
        val item = adapterView.getItemAtPosition(i) as Main
        mCityAutocomplete.setText(item.cityName)
        showAlertForError("Temp of ${item.cityName} is ${item.temp} degrees")
    }

    private fun showAlertForError(alertMsg:String)
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.app_name))
        builder.setMessage(alertMsg)
        builder.setCancelable(false)
        builder.setPositiveButton("Ok") { dialog, which ->
            mCityAutocomplete.setText("")
            dialog.cancel()
        }
        builder.show()
    }

}
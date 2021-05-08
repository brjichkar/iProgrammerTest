package com.iprogrammertest.ui_section.city_weather_section.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.iprogrammertest.R
import com.iprogrammertest.ui_section.city_weather_section.model.Main
import java.text.SimpleDateFormat
import java.util.*

class AdapterCityRecords (private var previousSearchList: MutableList<Main>) : androidx.recyclerview.widget.RecyclerView.Adapter<AdapterCityRecords.CityItemAdapter>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityItemAdapter {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.row_city_temp_details, parent, false)
        return CityItemAdapter(itemView)
    }

    override fun onBindViewHolder(holder: CityItemAdapter, position: Int) {
        holder.cityName.text = previousSearchList[position].cityName
        holder.cityTemp.text = previousSearchList[position].temp.toString()
        holder.cityMinTemp.text = previousSearchList[position].temp_min.toString()
        holder.cityMaxTemp.text = previousSearchList[position].temp_max.toString()
        holder.cityrecordedDay.text = deviceTimeStampForDisplay(previousSearchList[position].storeTime)
    }

    override fun getItemCount(): Int {
        return previousSearchList.size
    }

    class CityItemAdapter(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView){
        var cityName: TextView = itemView.findViewById(R.id.tv_city_name)
        var cityTemp: TextView = itemView.findViewById(R.id.tv_city_temp)
        var cityMinTemp: TextView = itemView.findViewById(R.id.tv_city_min_temp)
        var cityMaxTemp: TextView = itemView.findViewById(R.id.tv_city_max_temp)
        var cityrecordedDay: TextView = itemView.findViewById(R.id.tv_city_record_day)
    }

    private fun deviceTimeStampForDisplay(recordedTime:String): String {
        val df = SimpleDateFormat("yyyy-MMM-dd HH:mm a", Locale.ENGLISH)
        df.timeZone = TimeZone.getTimeZone("UTC")
        val date: Date = df.parse(recordedTime)
        df.timeZone = TimeZone.getDefault()
        return df.format(date)
    }
}
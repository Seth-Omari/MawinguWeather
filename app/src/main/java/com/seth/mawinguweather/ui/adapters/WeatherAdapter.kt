package com.seth.mawinguweather.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.seth.mawinguweather.R
import com.seth.mawinguweather.data.model.DatabaseWeatherData
import com.seth.mawinguweather.databinding.ItemWeatherDataBinding
import com.seth.mawinguweather.util.AppUtils

class WeatherAdapter :
    RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {

    private var weatherDetailList = ArrayList<DatabaseWeatherData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemWeatherDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_weather_data,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(weatherDetailList[position])
    }

    override fun getItemCount(): Int = weatherDetailList.size

    fun setData(
        newWeatherDetail: List<DatabaseWeatherData>
    ) {
        weatherDetailList.clear()
        weatherDetailList.addAll(newWeatherDetail)
        notifyDataSetChanged()
    }

    fun filterList(filteredList: ArrayList<DatabaseWeatherData>) {
        weatherDetailList = filteredList
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding: ItemWeatherDataBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bindItems(weatherData: DatabaseWeatherData) {
            binding.apply {
                val iconCode = weatherData.icon?.replace("n", "d")

                AppUtils.setGlideImage(
                    imageWeatherSymbol,
                    "http://openweathermap.org/img/wn/" + "${iconCode}@4x.png"
                )
                textCityName.text =
                    "${weatherData.cityName?.capitalize()}, ${weatherData.countryName}"
                textTemperature.text = weatherData.temp.toString()
                textDateTime.text = weatherData.dateTime
            }
        }
    }
}

package com.seth.mawinguweather.data.repositories

import com.seth.mawinguweather.data.local.WeatherDatabase
import com.seth.mawinguweather.data.model.DatabaseWeatherData
import com.seth.mawinguweather.data.model.WeatherDataResponse
import com.seth.mawinguweather.data.network.ApiRequest
import com.seth.mawinguweather.data.network.ApiService
import com.seth.mawinguweather.data.network.WeatherApiNetwork

class WeatherRepository(
    private val api: ApiService,
    private val db: WeatherDatabase
) : ApiRequest() {

    suspend fun fetchCityWeather(cityName: String): WeatherDataResponse = apiRequest {
        api.getCityWeatherData(cityName)
    }

    suspend fun addWeather(weatherData: DatabaseWeatherData) {
        db.getWeatherDao().addWeather(weatherData)
    }

    suspend fun fetchWeatherDetail(cityName: String): List<DatabaseWeatherData>? =
        db.getWeatherDao().fetchWeatherByCity(cityName)

    suspend fun fetchAllWeatherDetails(): List<DatabaseWeatherData> =
        db.getWeatherDao().fetchAllWeatherData()

}
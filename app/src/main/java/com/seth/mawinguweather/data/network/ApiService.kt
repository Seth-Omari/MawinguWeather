package com.seth.mawinguweather.data.network

import com.seth.mawinguweather.data.model.WeatherDataResponse
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ApiService {
    @GET("weather")
    suspend fun getCityWeatherData(
        @Query("q") query: String,
        @Query("units") units: String = "metric",
        @Query("appid") access_key: String = "4d8c6d5b86ef17b955ab9c2ce0031fcf"
    ): Response<WeatherDataResponse>


    companion object {
        operator fun invoke(): ApiService {


            return Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}

object WeatherApiNetwork {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://api.weatherstack.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val currentWeather = retrofit.create(ApiService::class.java)

}
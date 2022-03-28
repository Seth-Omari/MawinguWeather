package com.seth.mawinguweather.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.seth.mawinguweather.data.model.DatabaseWeatherData

@Dao
interface WeatherDataDao {

    /**
     * Duplicate values are replaced in the table.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeather(weatherData: DatabaseWeatherData)

    @Query("SELECT * FROM ${DatabaseWeatherData.TABLE_NAME} WHERE cityName = :cityName")
    suspend fun fetchWeatherByCity(cityName: String): List<DatabaseWeatherData>?

    @Query("SELECT * FROM ${DatabaseWeatherData.TABLE_NAME}")
    suspend fun fetchAllWeatherData(): List<DatabaseWeatherData>

}

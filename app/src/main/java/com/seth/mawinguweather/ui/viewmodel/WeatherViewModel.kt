package com.seth.mawinguweather.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.seth.mawinguweather.data.model.DatabaseWeatherData
import com.seth.mawinguweather.data.model.WeatherDataResponse
import com.seth.mawinguweather.data.repositories.WeatherRepository
import com.seth.mawinguweather.util.AppUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class WeatherViewModel(private val repository: WeatherRepository) :
    ViewModel() {

    /**
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    private val _weatherDataList =
        MutableLiveData<List<DatabaseWeatherData>>()
    val weatherDataList: LiveData<List<DatabaseWeatherData>>
        get() = _weatherDataList
    private val _filteredWeatherDataList =
        MutableLiveData<List<DatabaseWeatherData>>()
    val filteredWeatherDataList: LiveData<List<DatabaseWeatherData>>
        get() = _weatherDataList
    private lateinit var weatherResponse: WeatherDataResponse


    init {
        fetchAllWeatherDataFromDb()
    }

     fun filterCitiesWeatherData(text: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDataList = repository.fetchAllWeatherDetails()
            for (item in weatherDataList) {
                if (item.cityName?.lowercase(Locale.getDefault())?.contains(text.lowercase(Locale.getDefault())) == true) {
                    _weatherDataList.postValue(
                        listOf(item)
                    )
                }
            }
        }
    }

    private val cities =
        arrayListOf("Nairobi", "Kisumu", "Lagos", "kampala", "Nakuru", "New York", "Mombasa")

    fun fetchPopularMovies() {
        viewModelScope.launch(Dispatchers.IO) {
            for (city in cities) {
                try {
                    weatherResponse =
                        repository.fetchCityWeather(city)
                    addWeatherDetailIntoDb(weatherResponse)
                    fetchAllWeatherDataFromDb()
                    _eventNetworkError.postValue(false)
                    _isNetworkErrorShown.postValue(false)
                } catch (e: Exception) {
                    _eventNetworkError.postValue(true)
                }
            }

        }
    }

    /**
     * Resets the network error flag.
     */
    fun onNetworkErrorShown() {
        _isNetworkErrorShown.value = true
    }

    private fun fetchAllWeatherDataFromDb() {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherDetailList = repository.fetchAllWeatherDetails()
            withContext(Dispatchers.Main) {
                _weatherDataList.postValue(
                    weatherDetailList
                )
            }
        }
    }

    private suspend fun addWeatherDetailIntoDb(weatherResponse: WeatherDataResponse) {
        val weatherData = DatabaseWeatherData()
        weatherData.id = weatherResponse.id
        weatherData.icon = weatherResponse.weather.first().icon
        weatherData.cityName = weatherResponse.name
        weatherData.countryName = weatherResponse.sys.country
        weatherData.temp = weatherResponse.main.temp
        weatherData.dateTime = AppUtils.getCurrentDateTime("E, d MMM yyyy HH:mm:ss")
        repository.addWeather(weatherData)
    }

    /**
     * Factory for constructing WeatherViewModel with parameter
     */
    class Factory(private val repo: WeatherRepository) : ViewModelProvider.NewInstanceFactory() {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return WeatherViewModel(repo) as T
        }
    }


}
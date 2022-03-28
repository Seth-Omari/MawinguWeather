package com.seth.mawinguweather

import android.app.Application
import com.seth.mawinguweather.data.local.WeatherDatabase
import com.seth.mawinguweather.data.network.ApiService
import com.seth.mawinguweather.data.repositories.WeatherRepository
import com.seth.mawinguweather.ui.viewmodelfactory.WeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MawinguWeatherApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }


}
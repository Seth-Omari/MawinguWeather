package com.seth.mawinguweather.ui.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.seth.mawinguweather.R
import com.seth.mawinguweather.data.local.WeatherDatabase
import com.seth.mawinguweather.data.model.DatabaseWeatherData
import com.seth.mawinguweather.data.network.ApiService
import com.seth.mawinguweather.data.repositories.WeatherRepository
import com.seth.mawinguweather.databinding.FragmentWeatherBinding
import com.seth.mawinguweather.ui.adapters.WeatherAdapter
import com.seth.mawinguweather.ui.viewmodel.WeatherViewModel
import com.seth.mawinguweather.ui.viewmodelfactory.WeatherViewModelFactory
import org.kodein.di.generic.instance


class WeatherFragment : Fragment() {

    private val api = ApiService.invoke()
    private lateinit var weatherAdapter: WeatherAdapter
    private lateinit var binding: FragmentWeatherBinding
    private val viewModel: WeatherViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            this,
            WeatherViewModel.Factory(
                WeatherRepository(
                    api,
                    WeatherDatabase.invoke(activity.application)
                )
            )
        )
            .get(WeatherViewModel::class.java)
    }

    private val searchTextWatcher = object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            viewModel.filterCitiesWeatherData(editable.toString())
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_weather,
            container,
            false
        )
        // Set the lifecycleOwner so DataBinding can observe LiveData
        binding.lifecycleOwner = viewLifecycleOwner
        weatherAdapter = WeatherAdapter()

        binding.root.findViewById<RecyclerView>(R.id.recycler_view_searched_city_temperature)
            .apply {
                layoutManager = LinearLayoutManager(context)
                adapter = weatherAdapter
            }
        binding.inputFindCityWeather.addTextChangedListener(searchTextWatcher)
        binding.viewModel = viewModel
        // Observer for the network error.
        viewModel.eventNetworkError.observe(
            viewLifecycleOwner,
            Observer<Boolean> { isNetworkError ->
                if (isNetworkError) onNetworkError()
            })
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initializeRecyclerView()

        viewModel.fetchWeatherData()
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.fetchWeatherData()
            binding.swipeRefresh.isRefreshing = false
        }
        viewModel.weatherDataList.observe(
            viewLifecycleOwner,
            Observer<List<DatabaseWeatherData>> { weatherData ->
                weatherAdapter.setData(weatherData)
            })

    }

    private fun onNetworkError() {
        if (!viewModel.isNetworkErrorShown.value!!) {
            Toast.makeText(activity, "Network Error", Toast.LENGTH_LONG).show()
            viewModel.onNetworkErrorShown()
        }
    }

}
package com.example.weatherapplication

import com.example.weatherapplication.di.DaggerTestComponent
import com.example.weatherapplication.model.retrofit.geobd.CityDataItem
import com.example.weatherapplication.model.retrofit.geobd.GeoDBRetrofit
import com.example.weatherapplication.model.retrofit.openweather.OpenWeatherResponse
import com.example.weatherapplication.model.retrofit.openweather.OpenWeatherRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Test
import javax.inject.Inject

class OpenWeatherTest {
    @Inject lateinit var geoDbRetrofit: GeoDBRetrofit
    @Inject lateinit var openWeatherRetrofit: OpenWeatherRetrofit
    @Inject lateinit var item: CityDataItem
    init {
        DaggerTestComponent.create().inject(this)
    }
    @Test
    fun checkIfResponseExist() = runBlocking {
        val weatherResponse = openWeatherRetrofit.getWeather(item)
        assert(weatherResponse?.weather?.isNotEmpty() == true)
    }

    @Test
    fun checkIfItemNotInApi() = runBlocking{
        val weatherResponse = openWeatherRetrofit.getWeather(CityDataItem("", ""))
        assert(weatherResponse == OpenWeatherResponse.default)
    }
}
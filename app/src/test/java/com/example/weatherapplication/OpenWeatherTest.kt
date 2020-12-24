package com.example.weatherapplication

import com.example.weatherapplication.di.DaggerTestComponent
import com.example.weatherapplication.model.retrofit.geobd.GeoDBRetrofit
import com.example.weatherapplication.model.retrofit.openweather.OpenWeatherRetrofit
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import javax.inject.Inject

class OpenWeatherTest {
    @Inject lateinit var geoDbRetrofit: GeoDBRetrofit
    @Inject lateinit var openWeatherRetrofit: OpenWeatherRetrofit
    init {
        DaggerTestComponent.create().inject(this)
    }
    @Test
    fun checkIfResponseExist() = runBlocking {
        delay(1000)
        val response = geoDbRetrofit.getCity("Mosc")?.data?.get(1) ?: return@runBlocking
        val weatherResponse = openWeatherRetrofit.getWeather(response)
        assert(weatherResponse?.weather?.isNotEmpty() == true)
    }
}
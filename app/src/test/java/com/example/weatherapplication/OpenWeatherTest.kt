package com.example.weatherapplication

import com.example.weatherapplication.model.retrofit.geobd.GeoDBRetrofit
import com.example.weatherapplication.model.retrofit.openweather.OpenWeatherRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Test

class OpenWeatherTest{
    val geoDbRetrofit = GeoDBRetrofit()
    val openWeatherRetrofit = OpenWeatherRetrofit()

    @Test
    fun checkIfResponseExist() = runBlocking {
        val response = geoDbRetrofit.getCity("Mosc").data[0] ?: return@runBlocking
        val weatherResponse = openWeatherRetrofit.getWeather(response)
        assert(weatherResponse.weather?.isNotEmpty() == true)
    }
}
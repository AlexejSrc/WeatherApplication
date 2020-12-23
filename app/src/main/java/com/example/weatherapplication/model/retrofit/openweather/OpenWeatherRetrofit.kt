package com.example.weatherapplication.model.retrofit.openweather

import com.example.weatherapplication.model.retrofit.geobd.CityDataItem
import javax.inject.Inject

class OpenWeatherRetrofit @Inject constructor(){
    @Inject lateinit var openWeatherClient: OpenWeatherApi

    suspend fun getWeather(city: CityDataItem): OpenWeatherResponse {
        return openWeatherClient.getWeather(city.name+","+city.countryCode)
    }
}
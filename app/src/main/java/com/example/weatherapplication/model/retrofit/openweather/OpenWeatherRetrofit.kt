package com.example.weatherapplication.model.retrofit.openweather

import com.example.weatherapplication.model.retrofit.geobd.CityDataItem
import com.example.weatherapplication.model.retrofit.geobd.GeoDBResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class OpenWeatherRetrofit @Inject constructor(){
    @Inject lateinit var openWeatherClient: OpenWeatherApi

    suspend fun getWeather(city: CityDataItem): OpenWeatherResponse {
        return openWeatherClient.getWeather(city.name+","+city.countryCode)
    }
}
package com.example.weatherapplication.model.retrofit.openweather

import com.example.weatherapplication.model.retrofit.geobd.CityDataItem
import com.example.weatherapplication.model.retrofit.geobd.GeoDBResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class OpenWeatherRetrofit {
    val openWeatherClient = Retrofit
        .Builder()
        .baseUrl("https://api.openweathermap.org")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OpenWeatherApi::class.java)

    suspend fun getWeather(city: CityDataItem): OpenWeatherResponse {
        return openWeatherClient.getWeather(city.name+","+city.countryCode)
    }
}
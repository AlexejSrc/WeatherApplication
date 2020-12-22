package com.example.weatherapplication.model.retrofit.openweather

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi{

    @GET("data/2.5/weather")

    suspend fun getWeather(
        @Query("q") cityNameCountryCode: String,
        @Query("appid") key: String = OpenWeatherApi.key,
        @Query("units") units: String="metric") : OpenWeatherResponse

    companion object{
        private const val key = "7ae13f90dd5a7aead8ce54808ae46eaf"
    }
}
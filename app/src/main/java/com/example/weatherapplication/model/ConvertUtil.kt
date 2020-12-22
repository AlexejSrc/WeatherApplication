package com.example.weatherapplication.model

import com.example.weatherapplication.model.retrofit.openweather.OpenWeatherResponse
import com.example.weatherapplication.model.room.Temperature
import com.example.weatherapplication.model.room.Weather
import com.example.weatherapplication.model.room.WeatherInCityEntity

fun convertFromOpenWeatherToRoomEntity(response: OpenWeatherResponse): WeatherInCityEntity{
    return WeatherInCityEntity(
        response.name,
        response.sys.country,
        response.clouds?.all,
        response.main?.let {
            Temperature(
                it.temp,
                it.tempMin,
                it.humidity,
                it.pressure,
                it.feelsLike,
                it.tempMax
            )
        },
        response.weather?.let {
            Weather(
                it[0]?.description,
                it[0]?.main,
                it[0]?.icon
            )
        }
    )
}
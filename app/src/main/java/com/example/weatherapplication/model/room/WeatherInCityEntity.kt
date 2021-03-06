package com.example.weatherapplication.model.room

import androidx.room.Embedded
import androidx.room.Entity

@Entity(primaryKeys = ["city","countryCode"])
data class WeatherInCityEntity(
    val city: String,
    val countryCode: String,
    val clouds: Int?,
    @Embedded val temperature: Temperature?,
    @Embedded val weather: Weather?
){
    companion object{
        val default = WeatherInCityEntity("", "", null, null, null)
    }
}

data class Temperature(
    val temp: Double?,
    val tempMin: Double?,
    val humidity: Int?,
    val pressure: Int?,
    val feelsLike: Double?,
    val tempMax: Double?
)

data class Weather(
    val description: String?,
    val main: String?,
    val icon: String?
)
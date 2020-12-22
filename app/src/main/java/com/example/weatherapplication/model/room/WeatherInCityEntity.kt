package com.example.weatherapplication.model.room

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherInCityEntity(
    @PrimaryKey val id: Int,
    val city: String,
    val countryCode: String,
    val clouds: Int?,
    @Embedded val temperature: Temperature?,
    @Embedded val weather: Weather?

)

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
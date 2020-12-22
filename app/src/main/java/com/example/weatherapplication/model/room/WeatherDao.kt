package com.example.weatherapplication.model.room

import androidx.room.*

@Dao
interface WeatherDao{

    @Query("Select * from WeatherInCityEntity where city = :cityName and countryCode = :countryCode")
    suspend fun getWeatherByName(cityName: String, countryCode: String): WeatherInCityEntity

    @Update
    suspend fun updateWeather(entity: WeatherInCityEntity)

    @Delete
    suspend fun deleteWeather(entity: WeatherInCityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(entity: WeatherInCityEntity)
}
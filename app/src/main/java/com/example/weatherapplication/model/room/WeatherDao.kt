package com.example.weatherapplication.model.room

import androidx.room.*

@Dao
interface WeatherDao{

    @Query("Select * from WeatherInCityEntity where id = :id")
    fun getWeatherById(id: Int): WeatherInCityEntity

    @Update
    fun updateWeather(entity: WeatherInCityEntity)

    @Delete
    fun deleteWeather(entity: WeatherInCityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertWeather(entity: WeatherInCityEntity)
}
package com.example.weatherapplication.model

import com.example.weatherapplication.model.retrofit.geobd.CityDataItem
import com.example.weatherapplication.model.retrofit.openweather.OpenWeatherRetrofit
import com.example.weatherapplication.model.room.ApplicationDatabase
import com.example.weatherapplication.model.room.WeatherInCityEntity
import javax.inject.Inject

class Repository @Inject constructor(){
    @Inject lateinit var database: ApplicationDatabase
    @Inject lateinit var openWeatherRetrofit: OpenWeatherRetrofit

    suspend fun getEntityFromDatabase(item: CityDataItem): WeatherInCityEntity {
        return database.weatherDao.getWeatherByName(item.name, item.countryCode)
    }

    suspend fun getEntityFromRetrofit(item: CityDataItem): WeatherInCityEntity{
        val apiResponse = openWeatherRetrofit.getWeather(item)
        val convertedEntity = convertFromOpenWeatherToRoomEntity(apiResponse)
        database.weatherDao.insertWeather(convertedEntity)
        return convertedEntity
    }
}
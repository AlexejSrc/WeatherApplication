package com.example.weatherapplication.model

import com.example.weatherapplication.model.retrofit.geobd.CityDataItem
import com.example.weatherapplication.model.retrofit.geobd.GeoDBRetrofit
import com.example.weatherapplication.model.retrofit.openweather.OpenWeatherResponse
import com.example.weatherapplication.model.retrofit.openweather.OpenWeatherRetrofit
import com.example.weatherapplication.model.room.ApplicationDatabase
import com.example.weatherapplication.model.room.WeatherInCityEntity
import javax.inject.Inject

class Repository @Inject constructor(){
    @Inject lateinit var database: ApplicationDatabase
    @Inject lateinit var openWeatherRetrofit: OpenWeatherRetrofit
    @Inject lateinit var geoDbRetrofit: GeoDBRetrofit

    suspend fun getEntityFromDatabase(item: CityDataItem): WeatherInCityEntity? {
        return database.weatherDao.getWeatherByName(item.name, item.countryCode)
    }

    suspend fun getEntityFromNetwork(item: CityDataItem): WeatherInCityEntity?{
        val apiResponse = openWeatherRetrofit.getWeather(item)
        return apiResponse?.let {
            convertFromOpenWeatherToRoomEntity(apiResponse)
        } ?: return null
    }

    suspend fun loadNewEntityToDatabase(item: CityDataItem): OpenWeatherResponse?{
        val apiResponse = openWeatherRetrofit.getWeather(item)
        apiResponse?.let {
            if (apiResponse != OpenWeatherResponse.default){
                database.weatherDao.insertWeather(convertFromOpenWeatherToRoomEntity(apiResponse))
            }
        }
        return apiResponse
    }

    suspend fun removeEntityFromDatabase(item: CityDataItem){
        database.weatherDao.deleteWeather(item.name, item.countryCode)
    }

    suspend fun getAllWeatherFromDatabase(): List<WeatherInCityEntity>{
        return database.weatherDao.getAllWeather()
    }

    suspend fun getCitiesByQuery(query: String): List<CityDataItem>?{
        return geoDbRetrofit.getCity(query)?.data
    }


}
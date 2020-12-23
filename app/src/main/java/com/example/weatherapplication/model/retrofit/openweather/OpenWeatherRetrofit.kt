package com.example.weatherapplication.model.retrofit.openweather

import com.example.weatherapplication.model.retrofit.geobd.CityDataItem
import java.net.UnknownHostException
import javax.inject.Inject

class OpenWeatherRetrofit @Inject constructor(){
    @Inject lateinit var openWeatherClient: OpenWeatherApi

    suspend fun getWeather(city: CityDataItem): OpenWeatherResponse? {
        return try {
            openWeatherClient.getWeather(city.name+","+city.countryCode)
        }
        catch (exception: retrofit2.HttpException){
            return OpenWeatherResponse.default
        }
        catch (exception: UnknownHostException){
            return null
        }
    }
}
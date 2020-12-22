package com.example.weatherapplication.di

import com.example.weatherapplication.model.retrofit.geobd.GeoDBApi
import com.example.weatherapplication.model.retrofit.openweather.OpenWeatherApi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RetrofitModule{

    @Provides
    @Singleton
    fun getGeoDBApi(): GeoDBApi{
        return Retrofit
            .Builder()
            .baseUrl("https://wft-geo-db.p.rapidapi.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeoDBApi::class.java)
    }

    @Provides
    @Singleton
    fun getOpenWeatherApi(): OpenWeatherApi{
        return Retrofit
            .Builder()
            .baseUrl("https://api.openweathermap.org")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenWeatherApi::class.java)
    }
}
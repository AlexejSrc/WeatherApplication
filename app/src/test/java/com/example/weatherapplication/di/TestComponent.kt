package com.example.weatherapplication.di

import com.example.weatherapplication.*
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.retrofit.geobd.GeoDBRetrofit
import com.example.weatherapplication.model.retrofit.openweather.OpenWeatherRetrofit
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RetrofitModule::class, TestDatabaseModule::class])
interface TestComponent {
    fun provideRepository(): Repository
    fun provideGeoDb(): GeoDBRetrofit
    fun provideOpenWeather(): OpenWeatherRetrofit

    fun inject(geoDBTest: GeoDBTest)
    fun inject(openWeatherTest: OpenWeatherTest)
    fun inject(roomDBTest: RoomDBTest)
    fun inject(repositoryTest: RepositoryTest)
}
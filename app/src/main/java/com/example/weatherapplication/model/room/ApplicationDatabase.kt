package com.example.weatherapplication.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherInCityEntity::class], version = 1)
abstract class ApplicationDatabase: RoomDatabase() {
    abstract val weatherDao: WeatherDao
}
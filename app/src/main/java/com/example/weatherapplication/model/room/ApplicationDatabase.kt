package com.example.weatherapplication.model.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherInCityEntity::class], version = 1, exportSchema = false)
abstract class ApplicationDatabase: RoomDatabase() {
    abstract val weatherDao: WeatherDao
}
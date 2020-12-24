package com.example.weatherapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapplication.di.DaggerTestComponent
import com.example.weatherapplication.model.convertFromOpenWeatherToRoomEntity
import com.example.weatherapplication.model.retrofit.geobd.GeoDBRetrofit
import com.example.weatherapplication.model.retrofit.openweather.OpenWeatherRetrofit
import com.example.weatherapplication.model.room.ApplicationDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@RunWith(AndroidJUnit4::class)
class RoomDBTest{
    @Inject lateinit var database: ApplicationDatabase
    @Inject lateinit var geoDbRetrofit: GeoDBRetrofit
    @Inject lateinit var openWeatherRetrofit: OpenWeatherRetrofit
    init {
        DaggerTestComponent.create().inject(this)
    }

    @Test
    fun testDaoSaving() = runBlocking {
        withContext(Dispatchers.IO) {
            delay(1000)
            val response = geoDbRetrofit.getCity("Mosc")?.data?.get(1) ?: return@withContext
            openWeatherRetrofit.getWeather(response)?.let {
                val entity =convertFromOpenWeatherToRoomEntity(it)
                database.weatherDao.insertWeather(entity)
                val entity2 = database.weatherDao.getWeatherByName(entity.city, entity.countryCode)
                assert(entity == entity2)
            }
        }
    }
}
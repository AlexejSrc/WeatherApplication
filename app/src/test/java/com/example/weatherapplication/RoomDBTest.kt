package com.example.weatherapplication

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapplication.model.convertFromOpenWeatherToRoomEntity
import com.example.weatherapplication.model.retrofit.geobd.GeoDBRetrofit
import com.example.weatherapplication.model.retrofit.openweather.OpenWeatherRetrofit
import com.example.weatherapplication.model.room.ApplicationDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RoomDBTest {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val database = Room.inMemoryDatabaseBuilder(context, ApplicationDatabase::class.java).build()
    val geoDbRetrofit = GeoDBRetrofit()
    val openWeatherRetrofit = OpenWeatherRetrofit()
    @Test
    fun testDaoSaving() = runBlocking {
        withContext(Dispatchers.IO) {
            val response = geoDbRetrofit.getCity("Mosc").data[1] ?: return@withContext
            val weatherResponse = openWeatherRetrofit.getWeather(response)
            val entity =
                convertFromOpenWeatherToRoomEntity(
                    weatherResponse
                )
            database.weatherDao.insertWeather(entity)
            val entity2 = database.weatherDao.getWeatherByName(entity.city, entity.countryCode)
            assert(entity == entity2)
        }
    }
}
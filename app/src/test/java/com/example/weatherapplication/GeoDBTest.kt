package com.example.weatherapplication

import com.example.weatherapplication.di.DaggerTestComponent
import com.example.weatherapplication.model.retrofit.geobd.GeoDBRetrofit
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test
import javax.inject.Inject

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class GeoDBTest{
    @Inject lateinit var retrofit: GeoDBRetrofit
    init {
        DaggerTestComponent.create().inject(this)
    }

    @Test
    fun checkIfResponseExist() = runBlocking {
        delay(1000)
        val response = retrofit.getCity("Mosc")
        assert(response?.data?.isNotEmpty() == true)
    }

    @Test
    fun checkIfResponseReturnsEmptyList() = runBlocking {
        delay(1000)
        val response = retrofit.getCity("r12w3ecrew")
        assert(response?.data?.isEmpty() == true)
    }

}
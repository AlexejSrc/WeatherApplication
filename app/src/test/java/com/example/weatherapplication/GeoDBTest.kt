package com.example.weatherapplication

import com.example.weatherapplication.model.retrofit.geobd.GeoDBRetrofit
import kotlinx.coroutines.runBlocking
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class GeoDBTest {
    val retrofit =
        GeoDBRetrofit()
    @Test
    fun checkIfResponseExist() = runBlocking {
        val response = retrofit.getCity("Mosc")
        assert(response.data.isNotEmpty())
    }

    @Test
    fun checkIfResponseReturnsEmptyList() = runBlocking {
        val response = retrofit.getCity("r12w3ecrew")
        assert(response.data.isEmpty())
    }

}
package com.example.weatherapplication.model.retrofit.geobd

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class GeoDBRetrofit {

    val geoDbClient = Retrofit
        .Builder()
        .baseUrl("https://wft-geo-db.p.rapidapi.com")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeoDBApi::class.java)

    suspend fun getCity(query: String): GeoDBResponse {
        return try {
            geoDbClient.getCity(query)
        }
        catch (exception: retrofit2.HttpException) {
            GeoDBResponse(
                emptyList()
            )
        }
    }
}
package com.example.weatherapplication.model.retrofit.geobd

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface GeoDBApi {
    @Headers(apiKey)
    @GET("/v1/geo/cities")
    suspend fun getCity(@Query(value = "namePrefix") query: String): GeoDBResponse

    companion object{
        private const val apiKey = "X-Rapidapi-Key: " + "039df82defmsh9a74d6124422e6cp105b27jsn69b136d21781"
    }
}
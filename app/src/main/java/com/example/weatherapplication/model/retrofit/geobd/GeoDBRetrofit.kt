package com.example.weatherapplication.model.retrofit.geobd

import javax.inject.Inject

class GeoDBRetrofit @Inject constructor() {

    @Inject lateinit var geoDbClient: GeoDBApi

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
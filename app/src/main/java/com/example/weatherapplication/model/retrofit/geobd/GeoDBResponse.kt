package com.example.weatherapplication.model.retrofit.geobd

import com.google.gson.annotations.SerializedName

data class GeoDBResponse(

	@field:SerializedName("data")
	val data: List<CityDataItem>

)

data class CityDataItem(

	@field:SerializedName("city")
	val name: String,

	@field:SerializedName("countryCode")
	val countryCode: String

)

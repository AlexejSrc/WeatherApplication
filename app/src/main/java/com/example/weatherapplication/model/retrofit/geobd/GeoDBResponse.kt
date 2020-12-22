package com.example.weatherapplication.model.retrofit.geobd

import com.google.gson.annotations.SerializedName

data class GeoDBResponse(

	@field:SerializedName("data")
	val data: List<CityDataItem?>

)

data class CityDataItem(

	@field:SerializedName("country")
	val country: String? = null,

	@field:SerializedName("city")
	val city: String? = null,

	@field:SerializedName("countryCode")
	val countryCode: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("type")
	val type: String? = null

)

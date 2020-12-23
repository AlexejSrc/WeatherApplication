package com.example.weatherapplication.model

data class SearchAdapterItem(
        val city: String,
        val countryCode: String,
        var isInFavourites: Boolean
)
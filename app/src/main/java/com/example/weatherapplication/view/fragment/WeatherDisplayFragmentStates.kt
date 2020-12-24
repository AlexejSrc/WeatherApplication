package com.example.weatherapplication.view.fragment

import com.example.weatherapplication.model.room.WeatherInCityEntity

sealed class WeatherDisplayFragmentStates {
    data class GotElement(val weatherInCityEntity: WeatherInCityEntity): WeatherDisplayFragmentStates()
    object Loading: WeatherDisplayFragmentStates()
    object NoConnection: WeatherDisplayFragmentStates()
    object NoElement: WeatherDisplayFragmentStates()
    object InitialState: WeatherDisplayFragmentStates()
}
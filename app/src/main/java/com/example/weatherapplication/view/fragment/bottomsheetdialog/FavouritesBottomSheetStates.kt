package com.example.weatherapplication.view.fragment.bottomsheetdialog

import com.example.weatherapplication.model.room.WeatherInCityEntity

sealed class FavouritesBottomSheetStates {
    class GotResult(val data: List<WeatherInCityEntity?>): FavouritesBottomSheetStates()
    object Loading: FavouritesBottomSheetStates()
    object EmptyList: FavouritesBottomSheetStates()
}
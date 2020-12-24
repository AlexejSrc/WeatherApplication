package com.example.weatherapplication.view.fragment.bottomsheetdialog

import com.example.weatherapplication.model.SearchAdapterItem

sealed class SearchBottomSheetStates{
    object Loading: SearchBottomSheetStates()
    object NoConnection: SearchBottomSheetStates()
    object NoData: SearchBottomSheetStates()
    object DefaultState: SearchBottomSheetStates()
    object CantAddItemToFavouritesToast: SearchBottomSheetStates()
    object ShowNoConnectionToast: SearchBottomSheetStates()
    class GotResults(val result: List<SearchAdapterItem>): SearchBottomSheetStates()
}
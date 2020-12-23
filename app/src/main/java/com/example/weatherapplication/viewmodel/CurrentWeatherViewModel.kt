package com.example.weatherapplication.viewmodel

import androidx.lifecycle.*
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.SearchAdapterItem
import com.example.weatherapplication.model.retrofit.geobd.CityDataItem
import com.example.weatherapplication.model.room.WeatherInCityEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import javax.inject.Inject

class CurrentWeatherViewModel : ViewModel(){
    private val currentWeatherState = MutableLiveData<WeatherInCityEntity?>()
    private val itemInFavourites = MutableLiveData<Boolean>(false)
    private val weathersInFavouritesList = MutableLiveData<List<WeatherInCityEntity?>>()
    private var weatherAfterQueryList = MutableLiveData<List<SearchAdapterItem>?>()

    @Inject lateinit var repository: Repository

    fun getCurrentWeather(): MutableLiveData<WeatherInCityEntity?> = currentWeatherState

    fun getWeatherList(): LiveData<List<WeatherInCityEntity?>> = weathersInFavouritesList

    fun getWeatherAfterQueryList(): LiveData<List<SearchAdapterItem>?> = weatherAfterQueryList

    fun getItemInFavourites(): LiveData<Boolean> = itemInFavourites

    fun updateCurrentWeather(item: CityDataItem){
        viewModelScope.launch(IO) {
            currentWeatherState.postValue(repository.getEntityFromDatabase(item))
            repository.getEntityFromNetwork(item)?.let {
                currentWeatherState.postValue(it)
            }
            currentWeatherState.value?.let {
                itemInFavourites.postValue(itemInDatabase(CityDataItem(it.city, it.countryCode)))
            }
        }
    }

    fun addToFavourites(item: CityDataItem){
        viewModelScope.launch(IO) {
            repository.loadNewEntityToDatabase(item)
            updateWeatherList()
        }
    }

    fun changeCurrentItemFavourites(){
        currentWeatherState.value?.let {
            val item = CityDataItem(it.city, it.countryCode)
            if(itemInDatabase(item)){
                removeFromFavourites(item)
                itemInFavourites.value = false

            } else{
                addToFavourites(item)
                itemInFavourites.value = true
            }
        }
    }

    fun removeFromFavourites(item: CityDataItem){
        viewModelScope.launch(IO) {
            repository.removeEntityFromDatabase(item)
            updateWeatherList()
        }
    }


    fun updateWeatherList(){
        viewModelScope.launch(IO) {
            weathersInFavouritesList.postValue(repository.getAllWeatherFromDatabase())
        }
    }

    private var searchFor = ""
    fun getQueryWithDelay(query: String, delay: Long = 700, minChars: Int = 4){
        if (query.length >= minChars) {
            val searchText = query.trim()
            if (searchText == searchFor)
                return
            searchFor = searchText
            viewModelScope.launch(Dispatchers.Default) {
                delay(delay)  //debounce timeOut
                if (searchText != searchFor)
                    return@launch
                getNewQueryFromRetrofit(searchText)
                searchFor = ""
            }
        }
    }

    fun clearSearchData(){
        weatherAfterQueryList = MutableLiveData()
    }

    private fun getNewQueryFromRetrofit(query: String){
        viewModelScope.launch(IO) {
            val items = repository.getCitiesByQuery(query)
            weatherAfterQueryList.postValue(
                    items?.map {
                        SearchAdapterItem(it.name, it.countryCode, itemInDatabase(it))
                    }?.distinct()
            )
        }
    }

    private fun itemInDatabase(item: CityDataItem): Boolean{
        weathersInFavouritesList.value?.let {
            for (i in it){
                if (i?.city==item.name && i.countryCode == item.countryCode){
                    return true
                }
            }
            return false
        } ?: return false
    }
}
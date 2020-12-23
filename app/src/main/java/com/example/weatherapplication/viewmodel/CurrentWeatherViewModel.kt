package com.example.weatherapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.retrofit.geobd.CityDataItem
import com.example.weatherapplication.model.room.WeatherInCityEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import javax.inject.Inject

class CurrentWeatherViewModel : ViewModel(){
    private val currentWeather = MutableLiveData<WeatherInCityEntity?>()
    private val weathersInFavouritesList = MutableLiveData<List<WeatherInCityEntity?>>()
    @Inject lateinit var repository: Repository

    fun getCurrentWeather(): LiveData<WeatherInCityEntity?> = currentWeather

    fun getWeatherList(): LiveData<List<WeatherInCityEntity?>> = weathersInFavouritesList

    fun updateCurrentWeather(item: CityDataItem){
        CoroutineScope(IO).launch {
            currentWeather.postValue(repository.getEntityFromDatabase(item))
            currentWeather.postValue(repository.getEntityFromNetwork(item))
        }
    }

    fun addToFavourites(item: CityDataItem){
        CoroutineScope(IO).launch {
            repository.loadNewEntityToDatabase(item)
        }
    }

    fun removeFromFavourites(item: WeatherInCityEntity){
        CoroutineScope(IO).launch {
            repository.removeEntityFromDatabase(item)
        }
    }

    fun updateWeatherList(){
        CoroutineScope(IO).launch {
            weathersInFavouritesList.postValue(repository.getAllWeatherFromDatabase())
        }

    }

}
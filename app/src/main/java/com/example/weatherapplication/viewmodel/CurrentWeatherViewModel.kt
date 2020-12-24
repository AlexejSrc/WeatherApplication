package com.example.weatherapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.SearchAdapterItem
import com.example.weatherapplication.model.convertAdapterItemToCityDataItem
import com.example.weatherapplication.model.retrofit.geobd.CityDataItem
import com.example.weatherapplication.model.retrofit.openweather.OpenWeatherResponse
import com.example.weatherapplication.model.room.WeatherInCityEntity
import com.example.weatherapplication.view.fragment.WeatherDisplayFragmentStates
import com.example.weatherapplication.view.fragment.bottomsheetdialog.FavouritesBottomSheetStates
import com.example.weatherapplication.view.fragment.bottomsheetdialog.SearchBottomSheetStates
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CurrentWeatherViewModel : ViewModel(){
    //Weather display observables
    private val currentWeatherState = MutableLiveData<WeatherDisplayFragmentStates>(WeatherDisplayFragmentStates.InitialState)
    private val currentWeatherStateAction =  MutableLiveData<WeatherDisplayFragmentStates?>(null)
    private val itemInFavourites = MutableLiveData<Boolean>(false)
    //Favourites observables
    private val weathersInFavouritesState = MutableLiveData<FavouritesBottomSheetStates>()
    private val weathersInFavouritesAction = MutableLiveData<FavouritesBottomSheetStates?>()
    //Search observables
    private var weatherAfterQueryList = MutableLiveData<SearchBottomSheetStates>(SearchBottomSheetStates.DefaultState)
    private var weatherAfterQueryStateAction = MutableLiveData<SearchBottomSheetStates?>(null)
    //
    private var copyOfDataFromDB = listOf<WeatherInCityEntity?>()
    @Inject lateinit var repository: Repository

    fun getCurrentWeather(): MutableLiveData<WeatherDisplayFragmentStates> = currentWeatherState

    fun getCurrentWeatherStateAction(): MutableLiveData<WeatherDisplayFragmentStates?> = currentWeatherStateAction

    fun getWeatherFavouritesState(): LiveData<FavouritesBottomSheetStates> = weathersInFavouritesState

    fun getWeatherFavouritesAction(): LiveData<FavouritesBottomSheetStates?> = weathersInFavouritesAction

    fun getWeatherAfterQueryList(): LiveData<SearchBottomSheetStates> = weatherAfterQueryList

    fun getWeatherAfterQueryStateAction(): LiveData<SearchBottomSheetStates?> = weatherAfterQueryStateAction

    fun getItemInFavourites(): LiveData<Boolean> = itemInFavourites

    fun updateCurrentWeather(item: CityDataItem){
        viewModelScope.launch(IO) {
            repository.getEntityFromDatabase(item)?.let {
                currentWeatherState.postValue(WeatherDisplayFragmentStates.GotElement(it))
            }
            currentWeatherStateAction.postValue(WeatherDisplayFragmentStates.Loading)
            repository.getEntityFromNetwork(item)?.let {
                if (it==WeatherInCityEntity.default){
                    currentWeatherState.postValue(WeatherDisplayFragmentStates.NoElement)
                }
                else{
                    currentWeatherState.postValue(WeatherDisplayFragmentStates.GotElement(it))
                }
            } ?: let {
                if (notTheSameItem(item)){
                    currentWeatherState.postValue(WeatherDisplayFragmentStates.NoConnection)
                }
            }
            currentWeatherStateAction.postValue(null)
            if(currentWeatherState.value is WeatherDisplayFragmentStates.GotElement){
                val currentItem = currentWeatherState.value as WeatherDisplayFragmentStates.GotElement
                itemInFavourites.postValue(itemInDatabase(CityDataItem(currentItem.weatherInCityEntity.city, currentItem.weatherInCityEntity.city)))
            }
            withContext(Dispatchers.Main){
                checkCurrentFavouritesSync()
            }

        }
    }

    fun notTheSameItem(item: CityDataItem): Boolean{
        return try{
            val currentItem = currentWeatherState.value as WeatherDisplayFragmentStates.GotElement
            item.name != currentItem.weatherInCityEntity.city || item.countryCode != currentItem.weatherInCityEntity.countryCode
        }
        catch (exception: Exception){
            false
        }
    }

    fun addToFavouritesFromSearch(item: SearchAdapterItem, syncCallback: ()->Unit){
        viewModelScope.launch(IO) {
            val response = repository.loadNewEntityToDatabase(convertAdapterItemToCityDataItem(item))
            withContext(Dispatchers.Main){
                when(response){
                    null ->{
                    weatherAfterQueryStateAction.value = SearchBottomSheetStates.ShowNoConnectionToast
                    weatherAfterQueryStateAction.value = null
                    }
                    OpenWeatherResponse.default ->{
                        weatherAfterQueryStateAction.value = SearchBottomSheetStates.CantAddItemToFavouritesToast
                        weatherAfterQueryStateAction.value = null
                    }
                    else -> {
                        item.isInFavourites = true
                        syncCallback()
                        checkListAndCurrentItem()
                    }
                }
            }

        }
    }

    fun removeFromFavouritesFromSearch(item: SearchAdapterItem, syncCallback: ()->Unit){
        viewModelScope.launch(IO) {
            repository.removeEntityFromDatabase(convertAdapterItemToCityDataItem(item))
            item.isInFavourites = false
            withContext(Dispatchers.Main){
                syncCallback()
            }
            checkListAndCurrentItem()
        }
    }

    fun addToFavourites(item: CityDataItem){
        viewModelScope.launch(IO) {
            repository.loadNewEntityToDatabase(item)
            checkListAndCurrentItem()
        }
    }

    fun removeFromFavourites(item: CityDataItem){
        viewModelScope.launch(IO) {
            repository.removeEntityFromDatabase(item)
            checkListAndCurrentItem()
        }
    }

    fun changeCurrentItemFavourites(){
        if(currentWeatherState.value is WeatherDisplayFragmentStates.GotElement){
            val item = currentWeatherState.value as WeatherDisplayFragmentStates.GotElement
            val newItem = CityDataItem(item.weatherInCityEntity.city, item.weatherInCityEntity.countryCode)
            if(itemInDatabase(newItem)){
                removeFromFavourites(newItem)
            }
            else{
                addToFavourites(newItem)
            }
        }
    }

    fun checkCurrentFavouritesSync(){
        if(currentWeatherState.value is WeatherDisplayFragmentStates.GotElement){
            val item = currentWeatherState.value as WeatherDisplayFragmentStates.GotElement
            val newItem = CityDataItem(item.weatherInCityEntity.city, item.weatherInCityEntity.countryCode)
            itemInFavourites.value = itemInDatabase(newItem)
        }
    }

    fun checkListAndCurrentItem(){
        viewModelScope.launch(IO) {
            weathersInFavouritesAction.postValue(FavouritesBottomSheetStates.Loading)
            copyOfDataFromDB = repository.getAllWeatherFromDatabase()
            withContext(Dispatchers.Main){
                if(copyOfDataFromDB.isEmpty()){
                    weathersInFavouritesState.value = FavouritesBottomSheetStates.EmptyList
                }
                else{
                    weathersInFavouritesState.value = FavouritesBottomSheetStates.GotResult(copyOfDataFromDB)
                }
                weathersInFavouritesAction.value = null
                checkCurrentFavouritesSync()
            }

        }
    }

    private var searchFor = ""
    fun getQueryWithDelay(query: String, delayTime: Long = 700, minChars: Int = 4) = viewModelScope.launch(IO) {
        if (query.length >= minChars) {
            searchFor = query
            delay(delayTime/2)  //debounce timeOut
            setLoadingState()
            delay(delayTime/2)
            if (query != searchFor) {
                if (searchFor.length<minChars){
                    setDefaultSearchState()
                }
                return@launch
            }
            getNewQueryFromRetrofit(query)
        }
        else{
            searchFor = ""
            setDefaultSearchState()
            withContext(Dispatchers.Main){
                clearSearchData()
            }
        }
    }

    private suspend fun getNewQueryFromRetrofit(query: String){
        val items = repository.getCitiesByQuery(query)
        if (items?.isEmpty()==true){
            setNoDataSearchState()
            return
        }
        items
            ?.map { SearchAdapterItem(it.name, it.countryCode, itemInDatabase(it)) }
            ?.distinct()
            ?.let {
                withContext(Dispatchers.Main){
                    if (searchFor.isNotEmpty()) setResultSearchState(it)
                    else setDefaultSearchState()
                }
            }
            ?: setNoConnectionSearchState()
    }

    fun clearSearchData(){
        weatherAfterQueryList.value = SearchBottomSheetStates.DefaultState
    }

    private suspend fun setLoadingState(){
        withContext(Dispatchers.Main){
            weatherAfterQueryStateAction.value = SearchBottomSheetStates.Loading
        }
    }

    private suspend fun setNoDataSearchState(){
        withContext(Dispatchers.Main){
            weatherAfterQueryStateAction.value = null
            weatherAfterQueryList.value = SearchBottomSheetStates.NoData
        }
    }

    private suspend fun setDefaultSearchState(){
        withContext(Dispatchers.Main){
            weatherAfterQueryStateAction.value = null
            weatherAfterQueryList.value = SearchBottomSheetStates.DefaultState
        }
    }

    private suspend fun setNoConnectionSearchState(){
        withContext(Dispatchers.Main){
            weatherAfterQueryStateAction.value = null
            weatherAfterQueryList.value = SearchBottomSheetStates.NoConnection
        }
    }

    private suspend fun setResultSearchState(list: List<SearchAdapterItem>){
        withContext(Dispatchers.Main){
            weatherAfterQueryStateAction.value = null
            weatherAfterQueryList.value = SearchBottomSheetStates.GotResults(list)
        }
    }

    private fun itemInDatabase(item: CityDataItem): Boolean{
        for (i in copyOfDataFromDB){
            if (i?.city==item.name && i.countryCode == item.countryCode){
                return true
            }
        }
        return false
    }
}
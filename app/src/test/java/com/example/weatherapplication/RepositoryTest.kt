package com.example.weatherapplication

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.weatherapplication.di.DaggerTestComponent
import com.example.weatherapplication.model.Repository
import com.example.weatherapplication.model.convertFromOpenWeatherToRoomEntity
import com.example.weatherapplication.model.retrofit.geobd.CityDataItem
import kotlinx.coroutines.runBlocking
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters
import javax.inject.Inject

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4::class)
class RepositoryTest {
    @Inject lateinit var repository: Repository
    @Inject lateinit var item: CityDataItem
    init {
        DaggerTestComponent.create().inject(this)
    }

    @Test
    fun testLoadingFromRetrofitAndSaving() = runBlocking {
        val response = repository.loadNewEntityToDatabase(item)
        val copyOfResponse = repository.getEntityFromDatabase(item)
        response?.let {
            assert(convertFromOpenWeatherToRoomEntity(it) == copyOfResponse)
        } ?: assert(false)

    }

    @Test
    fun testItemsListRetrieving() = runBlocking {
        repository.loadNewEntityToDatabase(item)
        val items = repository.getAllWeatherFromDatabase()
        assert(items.isNotEmpty())
    }

    @Test
    fun testItemRemovingMethod()= runBlocking {
        repository.loadNewEntityToDatabase(item)
        repository.removeEntityFromDatabase(item)
        assert(repository.getAllWeatherFromDatabase().isEmpty())
    }


}
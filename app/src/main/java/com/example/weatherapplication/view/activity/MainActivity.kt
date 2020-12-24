package com.example.weatherapplication.view.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.weatherapplication.R
import com.example.weatherapplication.model.retrofit.geobd.CityDataItem
import com.example.weatherapplication.view.fragment.bottomsheetdialog.SearchBottomSheetDialog
import com.example.weatherapplication.viewmodel.CurrentWeatherViewModel
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import java.util.*


class MainActivity : AppCompatActivity() {
    private val REQUEST_CODE_FOREGROUND: Int = 333
    private val viewModel: CurrentWeatherViewModel by viewModels()
    private lateinit var geocoder: Geocoder
    private var permissionWasNotRequested = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val locale = Locale.Builder().setLanguage("en").setRegion("US").build()
        geocoder = Geocoder(this, locale)
        viewModel.checkListAndCurrentItem()
        registerForActivityResult()
        permissionWasNotRequested = savedInstanceState?.getBoolean(PERMISSION_WAS_NOT_REQUESTED) ?: true
        if (permissionWasNotRequested) {
            checkLocationPermission()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_activity_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.search_item -> launchSearchFragment()
        }
        return true
    }

    private fun checkLocationPermission(){
        permissionWasNotRequested = false
        if (hasLocationPermission()) {
            loadDataFromLocation()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE_FOREGROUND)
        }
    }
    private fun hasLocationPermission() = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

    private fun registerForActivityResult(){
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {granted ->
            if (granted && hasLocationPermission()){
                loadDataFromLocation()
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun loadDataFromLocation(){
        LocationServices.getFusedLocationProviderClient(this).requestLocationUpdates(
                LocationRequest.create().apply {
                    priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                },
                object: LocationCallback() {
                    override fun onLocationResult(p0: LocationResult?) {
                        if (p0 == null) {
                            return
                        }
                        for (location in p0.locations) {
                            openCityFromCoordinates(location)
                        }
                    }
                },
                null)
    }

    fun openCityFromCoordinates(location: Location?){
        if(location==null) return
        getCityFromCoordinates(location)?.let {
            viewModel.updateCurrentWeather(it)
        }
    }

    private fun getCityFromCoordinates(location: Location): CityDataItem?{
        return try {
            val data = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            CityDataItem(data[0].locality, data[0].countryCode)

        }
        catch (exception: Exception){
            null
        }
    }

    private fun launchSearchFragment(){
        if (supportFragmentManager.findFragmentByTag("search_dialog") == null){
            SearchBottomSheetDialog().show(supportFragmentManager, "search_dialog")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(PERMISSION_WAS_NOT_REQUESTED, permissionWasNotRequested)
        super.onSaveInstanceState(outState)
    }
}
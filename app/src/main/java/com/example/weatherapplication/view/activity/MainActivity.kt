package com.example.weatherapplication.view.activity

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.weatherapplication.MyApplication
import com.example.weatherapplication.R
import com.example.weatherapplication.viewmodel.CurrentWeatherViewModel


class MainActivity : AppCompatActivity() {
    private val viewModel: CurrentWeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MyApplication.component.inject(viewModel)
        checkLocationPermission()
    }


    private fun checkLocationPermission(){
        val service = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val enabled = service.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if (!enabled) {
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        }
    }
}
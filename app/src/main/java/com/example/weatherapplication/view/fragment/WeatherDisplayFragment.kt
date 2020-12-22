package com.example.weatherapplication.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.weatherapplication.R
import com.example.weatherapplication.model.room.WeatherInCityEntity
import com.example.weatherapplication.viewmodel.CurrentWeatherViewModel
import kotlinx.android.synthetic.main.fragment_weather_display.*
import java.util.*


class WeatherDisplayFragment : Fragment() {
    private val viewModel: CurrentWeatherViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather_display, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCurrentWeather().observe(viewLifecycleOwner,
            Observer {
                setFragment(it)
            })
    }

    fun setFragment(entity: WeatherInCityEntity?){
        when(entity){
            null->setEmptyFragmentState()
            else->setDataInFragment(entity)
        }
    }

    fun setEmptyFragmentState(){
        setAllViewsInvisible()
    }


    fun setDataInFragment(entity: WeatherInCityEntity){
        setAllViewsVisible()
        weather_image_view.setImageDrawable(ContextCompat.getDrawable(requireContext(), fromCodeToDrawable(entity.weather?.icon)))
        city_title_text_view.text = entity.city+", "+entity.countryCode
        temperature_text_view.text = entity.temperature?.temp?.toString()
        weather_status_text_view.text = entity.weather?.description
        additional_temperature_info.text =
            "Average temperature: "+entity.temperature?.tempMin.toString()+"-"+entity.temperature?.tempMax+
                    "\nHumidity: "+entity.temperature?.humidity
    }

    fun setAllViewsVisible(){
        weather_image_view.visibility = View.VISIBLE
        city_title_text_view.visibility = View.VISIBLE
        temperature_text_view.visibility = View.VISIBLE
        weather_status_text_view.visibility = View.VISIBLE
        additional_temperature_info.visibility = View.VISIBLE
    }

    fun setAllViewsInvisible(){
        weather_image_view.visibility = View.INVISIBLE
        city_title_text_view.visibility = View.INVISIBLE
        temperature_text_view.visibility = View.INVISIBLE
        weather_status_text_view.visibility = View.INVISIBLE
        additional_temperature_info.visibility = View.INVISIBLE
    }
}
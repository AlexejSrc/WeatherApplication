package com.example.weatherapplication.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.weatherapplication.MyApplication
import com.example.weatherapplication.R
import com.example.weatherapplication.model.room.WeatherInCityEntity
import com.example.weatherapplication.view.fragment.bottomsheetdialog.FavouritesBottomSheetDialog
import com.example.weatherapplication.viewmodel.CurrentWeatherViewModel
import kotlinx.android.synthetic.main.fragment_weather_display_layout.*


class WeatherDisplayFragment : Fragment() {
    private val viewModel: CurrentWeatherViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather_display_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MyApplication.component.inject(viewModel)
        setOnClickListeners()
        setLiveDataObservers()
    }

    private fun setOnClickListeners(){
        fragment_weather_all_favourites.setOnClickListener {
            launchFavouritesFragment()
        }
        fragment_weather_favourites_button.setOnClickListener {
            viewModel.changeCurrentItemFavourites()
        }
    }

    private fun setLiveDataObservers(){
        viewModel.getItemInFavourites().observe(viewLifecycleOwner, Observer {
            if(it){
                fragment_weather_favourites_button.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.in_favourites_icon))
            }
            else{
                fragment_weather_favourites_button.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.not_in_favourites_icon))
            }
        })
        viewModel.getCurrentWeatherStateAction().observe(viewLifecycleOwner,
                Observer {
                    setFragment(it)
                })
        viewModel.getCurrentWeather().observe(viewLifecycleOwner,
                Observer {
                    setFragment(it)
                })
    }

    private fun setFragment(states: WeatherDisplayFragmentStates?){
        when(states){
            is WeatherDisplayFragmentStates.GotElement -> setDataInFragment(states.weatherInCityEntity)
            WeatherDisplayFragmentStates.InitialState -> setInitialState()
            WeatherDisplayFragmentStates.NoElement-> setEmptyFragmentState()
            WeatherDisplayFragmentStates.Loading-> showLoading()
            WeatherDisplayFragmentStates.NoConnection->setNoConnectionState()
            null -> hideLoading()
        }
    }

    private fun setEmptyFragmentState(){
        setAllViewsInvisible()
        main_fragment_info.text = resources.getString(R.string.no_data)
    }

    private fun setNoConnectionState(){
        setAllViewsInvisible()
        main_fragment_info.text = resources.getString(R.string.no_connection)
    }

    private fun setInitialState(){
        setAllViewsInvisible()
        main_fragment_info.text = resources.getString(R.string.you_havent_selected)
    }

    private fun setDataInFragment(entity: WeatherInCityEntity){
        setAllViewsVisible()
        weather_image_view.setImageDrawable(ContextCompat.getDrawable(requireContext(), fromCodeToDrawable(entity.weather?.icon)))
        city_title_text_view.text = entity.city+", "+entity.countryCode
        temperature_text_view.text = entity.temperature?.temp?.toString()+"℃"
        weather_status_text_view.text = entity.weather?.description
        additional_temperature_info.text = getString(R.string.temperature_info,
                entity.temperature?.tempMin?.toInt(),
                entity.temperature?.tempMax?.toInt(),
                entity.temperature?.humidity,
                entity.clouds,
                entity.temperature?.pressure)
    }

    private fun setAllViewsVisible(){
        weather_image_view.visibility = View.VISIBLE
        city_title_text_view.visibility = View.VISIBLE
        temperature_text_view.visibility = View.VISIBLE
        weather_status_text_view.visibility = View.VISIBLE
        additional_temperature_info.visibility = View.VISIBLE
        main_fragment_info.visibility = View.GONE
    }

    private fun setAllViewsInvisible(){
        weather_image_view.visibility = View.INVISIBLE
        city_title_text_view.visibility = View.INVISIBLE
        temperature_text_view.visibility = View.INVISIBLE
        weather_status_text_view.visibility = View.INVISIBLE
        additional_temperature_info.visibility = View.INVISIBLE
        main_fragment_info.visibility = View.VISIBLE
    }

    private fun launchFavouritesFragment(){
        activity?.supportFragmentManager?.let {
            if (it.findFragmentByTag("favourites_dialog") == null){
                FavouritesBottomSheetDialog().show(it, "favourites_dialog")
            }
        }
    }

    private fun showLoading(){
        fragment_weather_progress_bar.visibility = View.VISIBLE
        main_fragment_info.visibility = View.INVISIBLE
    }

    private fun hideLoading(){
        fragment_weather_progress_bar.visibility = View.GONE
    }
}
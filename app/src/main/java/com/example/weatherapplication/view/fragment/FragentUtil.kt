package com.example.weatherapplication.view.fragment

import android.content.Context
import com.example.weatherapplication.R

fun fromCodeToDrawable(code: String?): Int{
    return when(code){
        "01d"->R.drawable.w01d2x
        "02d"->R.drawable.w02d2x
        "04d"->R.drawable.w04d2x
        "09d"->R.drawable.w09d2x
        "10d"->R.drawable.w10d2x
        "11d"->R.drawable.w11d2x
        "13d"->R.drawable.w13d2x
        "50d"->R.drawable.w50d2x
        "01n"->R.drawable.w01n2x
        "02n"->R.drawable.w02n2x
        "03n"->R.drawable.w03n2x
        "04n"->R.drawable.w04n2x
        "09n"->R.drawable.w09n2x
        "10n"->R.drawable.w10n2x
        "11n"->R.drawable.w11n2x
        "13n"->R.drawable.w13n2x
        "50n"->R.drawable.w50n2x
        else-> R.drawable.w03d2x
    }
}
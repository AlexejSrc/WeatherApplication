package com.example.weatherapplication

import com.example.weatherapplication.di.ApplicationComponent
import com.example.weatherapplication.di.DaggerApplicationComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication


class MyApplication: DaggerApplication(){
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        component = DaggerApplicationComponent.builder().application(this).build()
        return component
    }

    companion object{
        lateinit var component: ApplicationComponent
    }

}
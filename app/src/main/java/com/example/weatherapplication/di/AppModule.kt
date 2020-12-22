package com.example.weatherapplication.di

import android.app.Application
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun getContext(application: Application) = application.applicationContext

}
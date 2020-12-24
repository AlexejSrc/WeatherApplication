package com.example.weatherapplication.di

import android.content.Context
import android.location.Geocoder
import dagger.Module
import dagger.Provides
import java.util.*
import javax.inject.Singleton

@Module
class ActivityDependencyModule {
    @Singleton
    @Provides
    fun provideGeoCoder(context: Context) = Geocoder(context, Locale.ENGLISH)
}
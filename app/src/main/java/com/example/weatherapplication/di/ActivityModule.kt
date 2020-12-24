package com.example.weatherapplication.di

import com.example.weatherapplication.view.activity.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule{
    @ContributesAndroidInjector
    abstract fun contributeProductListActivity(): MainActivity
}
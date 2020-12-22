package com.example.weatherapplication.di

import android.app.Application
import android.content.Context
import com.example.weatherapplication.MyApplication
import com.example.weatherapplication.viewmodel.CurrentWeatherViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, AppModule::class])
interface ApplicationComponent : AndroidInjector<MyApplication> {

    fun provideApplicationContext(): Context
    fun inject(viewModel: CurrentWeatherViewModel)
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): ApplicationComponent
    }
}
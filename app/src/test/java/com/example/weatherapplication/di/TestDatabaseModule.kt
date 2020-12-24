package com.example.weatherapplication.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.weatherapplication.model.room.ApplicationDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class TestDatabaseModule{
    @Singleton
    @Provides
    fun provideDatabase(context: Context): ApplicationDatabase {
        return Room.inMemoryDatabaseBuilder(context, ApplicationDatabase::class.java).build()
    }

    @Singleton
    @Provides
    fun provideContext() = ApplicationProvider.getApplicationContext<Context>()
}
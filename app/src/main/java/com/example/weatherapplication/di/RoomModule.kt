package com.example.weatherapplication.di

import android.content.Context
import androidx.room.Room
import com.example.weatherapplication.model.room.ApplicationDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
@Module
class RoomModule{

    @Provides
    @Singleton
    fun getDatabase(context: Context): ApplicationDatabase{
        return Room.databaseBuilder(context, ApplicationDatabase::class.java, "MyDB").build()
    }
}
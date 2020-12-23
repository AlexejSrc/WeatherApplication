package com.example.weatherapplication.di

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.weatherapplication.R
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {
    @Singleton
    @Provides
    fun getContext(application: Application) = application.applicationContext

    @Singleton
    @Provides
    fun getDeleteBitmap(context: Context): Bitmap{
        return ContextCompat.getDrawable(context, R.drawable.delete_icon)!!.toBitmap(64, 64)
    }
}
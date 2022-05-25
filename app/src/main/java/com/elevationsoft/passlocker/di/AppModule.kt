package com.elevationsoft.passlocker.di

import android.content.Context
import com.elevationsoft.passlocker.utils.PrefUtils
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providePrefUtils(@ApplicationContext appContext: Context): PrefUtils {
        return PrefUtils(context = appContext)
    }
}
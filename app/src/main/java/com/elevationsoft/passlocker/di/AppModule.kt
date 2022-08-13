package com.elevationsoft.passlocker.di

import android.content.Context
import androidx.room.Room
import com.elevationsoft.passlocker.data.local.room.PassLockerDb
import com.elevationsoft.passlocker.data.local.room.RoomDao
import com.elevationsoft.passlocker.data.repository.CategoryRepoImpl
import com.elevationsoft.passlocker.data.repository.CredentialRepoImpl
import com.elevationsoft.passlocker.domain.repository.CategoryRepo
import com.elevationsoft.passlocker.domain.repository.CredentialRepo
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

    @Provides
    @Singleton
    fun provideRoomDbDao(@ApplicationContext appContext: Context): RoomDao {
        val dbName = "PASSLOCKER_DB"
        return Room.databaseBuilder(
            appContext,
            PassLockerDb::class.java,
            dbName
        ).build().roomDao()
    }

    @Provides
    @Singleton
    fun provideCategoryRepo(roomDao: RoomDao): CategoryRepo {
        return CategoryRepoImpl(roomDao)
    }

    @Provides
    @Singleton
    fun provideCredentialRepo(roomDao: RoomDao): CredentialRepo {
        return CredentialRepoImpl(roomDao)
    }

}
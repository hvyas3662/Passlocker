package com.elevationsoft.passlocker.di

import android.content.Context
import androidx.room.Room
import com.elevationsoft.passlocker.data.local.room.PassLockerDb
import com.elevationsoft.passlocker.data.local.room.RoomDao
import com.elevationsoft.passlocker.data.repository.CategoryRepoImpl
import com.elevationsoft.passlocker.data.repository.CredentialRepoImpl
import com.elevationsoft.passlocker.domain.use_cases.category.*
import com.elevationsoft.passlocker.domain.use_cases.credential.AddUpdateCredentialUC
import com.elevationsoft.passlocker.domain.use_cases.credential.DeleteCredentialUC
import com.elevationsoft.passlocker.domain.use_cases.credential.GetCredentialListUC
import com.elevationsoft.passlocker.domain.use_cases.credential.MarkUnMarkFavouriteCredentialUC
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
    fun provideCategoryRepo(roomDao: RoomDao): CategoryRepoImpl {
        return CategoryRepoImpl(roomDao)
    }

    @Provides
    @Singleton
    fun provideCredentialRepo(roomDao: RoomDao): CredentialRepoImpl {
        return CredentialRepoImpl(roomDao)
    }

    //Category use case providers
    @Provides
    @Singleton
    fun provideGetCategoryListUC(categoryRepo: CategoryRepoImpl): GetCategoryListUC {
        return GetCategoryListUC(categoryRepo)
    }

    @Provides
    @Singleton
    fun provideAddCategoryUC(categoryRepo: CategoryRepoImpl): AddUpdateCategoryUC {
        return AddUpdateCategoryUC(categoryRepo)
    }

    @Provides
    @Singleton
    fun provideGetLastCategoryPositionUC(categoryRepo: CategoryRepoImpl): GetLastCategoryPositionUC {
        return GetLastCategoryPositionUC(categoryRepo)
    }

    @Provides
    @Singleton
    fun provideDeleteCategoryUC(categoryRepo: CategoryRepoImpl): DeleteCategoryUC {
        return DeleteCategoryUC(categoryRepo)
    }

    @Provides
    @Singleton
    fun provideUpdateCategoryListPositionUC(categoryRepo: CategoryRepoImpl): UpdateCategoryListPositionUC {
        return UpdateCategoryListPositionUC(categoryRepo)
    }

    //Credential use case providers
    @Provides
    @Singleton
    fun provideAddUpdateCredentialUC(credentialRepoImpl: CredentialRepoImpl): AddUpdateCredentialUC {
        return AddUpdateCredentialUC(credentialRepoImpl)
    }

    @Provides
    @Singleton
    fun provideMarkUnMarkFavouriteCredentialUC(credentialRepoImpl: CredentialRepoImpl): MarkUnMarkFavouriteCredentialUC {
        return MarkUnMarkFavouriteCredentialUC(credentialRepoImpl)
    }

    @Provides
    @Singleton
    fun provideDeleteCredentialUC(credentialRepoImpl: CredentialRepoImpl): DeleteCredentialUC {
        return DeleteCredentialUC(credentialRepoImpl)
    }

    @Provides
    @Singleton
    fun provideGetCredentialListUC(credentialRepoImpl: CredentialRepoImpl): GetCredentialListUC {
        return GetCredentialListUC(credentialRepoImpl)
    }
}
package com.fitnessapp.di

import android.app.Application
import com.fitnessapp.data.repository.manager.LocalUserManagerImpl
import com.fitnessapp.domain.manager.LocalUserManager
import com.fitnessapp.domain.useCases.AppEntryUseCases
import com.fitnessapp.domain.useCases.ReadAppEntry
import com.fitnessapp.domain.useCases.ReadGender
import com.fitnessapp.domain.useCases.ReadWeight
import com.fitnessapp.domain.useCases.SaveAppEntry
import com.fitnessapp.domain.useCases.SaveGender
import com.fitnessapp.domain.useCases.SaveWeight
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideLocalUserManager(
        application: Application
    ) : LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun providesAppEntryUseCases(
        localUserManager: LocalUserManager
    ) = AppEntryUseCases(
        readAppEntry = ReadAppEntry(localUserManager),
        saveAppEntry = SaveAppEntry(localUserManager),
        readGender = ReadGender(localUserManager),
        readWeight = ReadWeight(localUserManager),
        saveGender = SaveGender(localUserManager),
        saveWeight = SaveWeight(localUserManager)
    )

}
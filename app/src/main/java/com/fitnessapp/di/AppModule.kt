package com.fitnessapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.fitnessapp.data.repository.local.AppDatabase
import com.fitnessapp.data.repository.local.WorkoutsProgressDao
import com.fitnessapp.data.repository.manager.WorkoutRepository
import com.fitnessapp.data.repository.manager.LocalUserManagerImpl
import com.fitnessapp.data.repository.manager.WorkoutProgressRepository
import com.fitnessapp.domain.manager.LocalUserManager
import com.fitnessapp.domain.useCases.AppEntryUseCases
import com.fitnessapp.domain.useCases.ReadAppEntry
import com.fitnessapp.domain.useCases.ReadGender
import com.fitnessapp.domain.useCases.ReadWeight
import com.fitnessapp.domain.useCases.SaveAppEntry
import com.fitnessapp.domain.useCases.SaveGender
import com.fitnessapp.domain.useCases.SaveWeight
import com.fitnessapp.domain.useCases.progressUseCases.GetWorkoutProgress
import com.fitnessapp.domain.useCases.progressUseCases.GetWorkoutProgressByDate
import com.fitnessapp.domain.useCases.progressUseCases.ProgressUseCases
import com.fitnessapp.domain.useCases.progressUseCases.SaveProgressUseCase
import com.fitnessapp.domain.useCases.progressUseCases.UpdateWorkoutsProgress
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
    fun provideLocalUserManager(
        application: Application
    ) : LocalUserManager = LocalUserManagerImpl(application)

    @Provides
    @Singleton
    fun provideWorkoutRepository(workoutsProgressDao: WorkoutsProgressDao): WorkoutProgressRepository {
        return WorkoutProgressRepository(workoutsProgressDao)
    }
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
    @Provides
    @Singleton
    fun providesDatabase(application: Application): AppDatabase{
        return Room.databaseBuilder(
            context = application,
            klass = AppDatabase::class.java,
            "workoutProgressDB"
        ).build()
    }
    @Provides
    @Singleton
    fun providesWorkoutProgressDao(database: AppDatabase): WorkoutsProgressDao = database.workoutsProgressDao

    @Provides
    @Singleton
    fun providesProgressUseCases(progressRepository: WorkoutProgressRepository, progressDao: WorkoutsProgressDao):ProgressUseCases{
        return  ProgressUseCases(
            getWorkoutProgress = GetWorkoutProgress(progressRepository),
            saveProgressUseCase = SaveProgressUseCase(progressRepository),
            getWorkoutProgressByDate = GetWorkoutProgressByDate(progressRepository),
            updateWorkoutsProgress = UpdateWorkoutsProgress(progressRepository)
        )
    }
}
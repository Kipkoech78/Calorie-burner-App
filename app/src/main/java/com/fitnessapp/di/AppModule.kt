package com.fitnessapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.fitnessapp.data.remote.dto.FoodAPi
import com.fitnessapp.data.remote.dto.foodmealsAPI
import com.fitnessapp.data.remote.dto.repo.MealRepositoryImp
import com.fitnessapp.data.repository.local.AppDatabase
import com.fitnessapp.data.repository.local.WorkoutsProgressDao
import com.fitnessapp.data.repository.manager.LocalUserManagerImpl
import com.fitnessapp.data.repository.manager.WorkoutProgressRepository
import com.fitnessapp.domain.manager.LocalUserManager
import com.fitnessapp.domain.mealsUseCases.GetMeals
import com.fitnessapp.domain.mealsUseCases.MealsUseCases
import com.fitnessapp.domain.mealsUseCases.SearchMeals
import com.fitnessapp.domain.repo.MealsRepository
import com.fitnessapp.domain.useCases.AppEntryUseCases
import com.fitnessapp.domain.useCases.ReadAppEntry
import com.fitnessapp.domain.useCases.ReadGender
import com.fitnessapp.domain.useCases.ReadWeight
import com.fitnessapp.domain.useCases.SaveAppEntry
import com.fitnessapp.domain.useCases.SaveGender
import com.fitnessapp.domain.useCases.SaveWeight
import com.fitnessapp.domain.useCases.progressUseCases.AddFavWorkouts
import com.fitnessapp.domain.useCases.progressUseCases.Delete
import com.fitnessapp.domain.useCases.progressUseCases.GetVafWorkouts
import com.fitnessapp.domain.useCases.progressUseCases.GetWorkoutProgress
import com.fitnessapp.domain.useCases.progressUseCases.GetWorkoutProgressByDate
import com.fitnessapp.domain.useCases.progressUseCases.ProgressUseCases
import com.fitnessapp.domain.useCases.progressUseCases.SaveProgressUseCase
import com.fitnessapp.domain.useCases.progressUseCases.UpdateWorkoutsProgress
import com.fitnessapp.utils.Constants.BASE_URL
import com.fitnessapp.utils.Constants.MEAL_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


val okHttpClient = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)  // Increase connection timeout
    .readTimeout(30, TimeUnit.SECONDS)     // Increase read timeout
    .writeTimeout(30, TimeUnit.SECONDS)    // Increase write timeout
    .build()
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
        )
            .fallbackToDestructiveMigration()
            .build()
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
            updateWorkoutsProgress = UpdateWorkoutsProgress(progressRepository),
            delete = Delete(progressRepository),
            getVafWorkouts = GetVafWorkouts(progressRepository),
            addFavWorkouts = AddFavWorkouts(progressRepository),
        )
    }
    @Provides
    @Singleton
    fun ProvideMealsRepository(mealsAPI: foodmealsAPI, foodAPi: FoodAPi): MealsRepository = MealRepositoryImp(mealsAPI, foodAPi)

    @Provides
    @Singleton
    fun provideMealsUseCases(mealsRepository: MealsRepository,): MealsUseCases {
        return  MealsUseCases(
            getMeals = GetMeals(mealsRepository),
            searchMeals = SearchMeals(mealsRepository),
            //getFood = GetFood(mealsRepository),

        )
    }
    @Provides
    @Singleton
    fun ProvidesMealsApi():foodmealsAPI{
        return Retrofit.Builder()
            .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(foodmealsAPI::class.java)
    }
    @Provides
    @Singleton
    fun ProvideMealNodeAPI(): FoodAPi {
        return Retrofit.Builder()
            .baseUrl(MEAL_BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(FoodAPi::class.java)
    }
}
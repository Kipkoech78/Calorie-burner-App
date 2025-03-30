package com.fitnessapp.data.remote.dto.repo

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import arrow.core.Either
import com.fitnessapp.data.mapper.toGeneralError
import com.fitnessapp.data.remote.dto.FoodAPi
import com.fitnessapp.data.remote.dto.MealsPagingSource
import com.fitnessapp.data.remote.dto.SearchMealsPagingSource
import com.fitnessapp.data.remote.dto.foodmealsAPI
import com.fitnessapp.domain.repo.MealsRepository
import com.fitnessapp.models.Food
import com.fitnessapp.models.FoodResponse
import com.fitnessapp.models.Meal
import com.fitnessapp.models.NetworkError
import com.fitnessapp.models.WorkoutVideo
import com.fitnessapp.models.genModels.Data
import kotlinx.coroutines.flow.Flow

class MealRepositoryImp(
    private val mealsAPI: foodmealsAPI,
    private val foodAPi: FoodAPi
): MealsRepository {
    override fun getMeals(firstName: String, ): Flow<PagingData<Meal>> {
        return  Pager(
            config = PagingConfig(10),
            pagingSourceFactory = {
                MealsPagingSource(foodMealsAPI = mealsAPI, )
            }
        ).flow
    }
    override fun SearchMeals(searchQuery: String): Flow<PagingData<Meal>> {
        return Pager(
            config = PagingConfig(10),
            pagingSourceFactory = {
                SearchMealsPagingSource( api = mealsAPI ,searchQuery = searchQuery)
            }
        ).flow
    }

    override suspend fun getRandomMeal(): Either<NetworkError, List<Data>> {
        return Either.catch {
            val mealsResponse = foodAPi.getRandomMeals() // Get full API response
            Log.d("API Response", "Meals received: $mealsResponse")

            mealsResponse.data // Extract the `data` list
        }.mapLeft {
            Log.e("API Error", "Error occurred: ${it.message}", it)
            it.toGeneralError() // Ensure this method correctly maps exceptions
        }
    }



}
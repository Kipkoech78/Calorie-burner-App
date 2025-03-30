package com.fitnessapp.domain.repo

import androidx.paging.PagingData
import arrow.core.Either
import com.fitnessapp.models.Food
import com.fitnessapp.models.FoodResponse
import com.fitnessapp.models.Meal
import com.fitnessapp.models.NetworkError
import com.fitnessapp.models.genModels.Data
import kotlinx.coroutines.flow.Flow

interface MealsRepository {
    fun getMeals(firstName: String ): Flow<PagingData<Meal>>
    fun SearchMeals(searchQuery: String): Flow<PagingData<Meal>>
    suspend fun getRandomMeal(): Either<NetworkError,List<Data> >

}
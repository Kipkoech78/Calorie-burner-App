package com.fitnessapp.data.remote.dto.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fitnessapp.data.remote.dto.MealsPagingSource
import com.fitnessapp.data.remote.dto.SearchMealsPagingSource
import com.fitnessapp.data.remote.dto.foodmealsAPI
import com.fitnessapp.domain.repo.MealsRepository
import com.fitnessapp.models.Meal
import kotlinx.coroutines.flow.Flow

class MealRepositoryImp(
    private val mealsAPI: foodmealsAPI
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
}
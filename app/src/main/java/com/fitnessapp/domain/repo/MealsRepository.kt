package com.fitnessapp.domain.repo

import androidx.paging.PagingData
import com.fitnessapp.models.Meal
import kotlinx.coroutines.flow.Flow

interface MealsRepository {
    fun getMeals(firstName: String ): Flow<PagingData<Meal>>
    fun SearchMeals(searchQuery: String): Flow<PagingData<Meal>>

}
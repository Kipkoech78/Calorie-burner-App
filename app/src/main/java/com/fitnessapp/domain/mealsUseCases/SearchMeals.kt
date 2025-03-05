package com.fitnessapp.domain.mealsUseCases

import androidx.paging.PagingData
import com.fitnessapp.domain.repo.MealsRepository
import com.fitnessapp.models.Meal
import kotlinx.coroutines.flow.Flow

class SearchMeals(private val newsRepository: MealsRepository) {
    operator fun invoke(searchQuery: String ): Flow<PagingData<Meal>> {
        return newsRepository.SearchMeals(searchQuery = searchQuery)
    }

}
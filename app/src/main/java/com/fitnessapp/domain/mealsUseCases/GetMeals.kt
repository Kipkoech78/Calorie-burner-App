package com.fitnessapp.domain.mealsUseCases

import androidx.paging.PagingData
import com.fitnessapp.domain.repo.MealsRepository
import com.fitnessapp.models.Meal
import kotlinx.coroutines.flow.Flow


class GetMeals(private val newsRepository: MealsRepository) {
    operator fun invoke(firstName: String ): Flow<PagingData<Meal>> {
        return newsRepository.getMeals(firstName = firstName)
    }
}
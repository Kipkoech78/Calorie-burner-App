package com.fitnessapp.presentation.dietetics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fitnessapp.domain.mealsUseCases.MealsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DieteticsViewModel @Inject constructor(private val mealsUseCases: MealsUseCases) : ViewModel() {
    // var state = mutableStateOf(HomeState())
    val meals = mealsUseCases.getMeals(
        firstName = "a"
    ).cachedIn(viewModelScope)
}
package com.fitnessapp.presentation.dietetics

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.fitnessapp.data.repository.manager.WorkoutRepository
import com.fitnessapp.domain.mealsUseCases.MealsUseCases
import com.fitnessapp.models.DayMeal
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DieteticsViewModel @Inject constructor(private val mealsUseCases: MealsUseCases, private val repository: WorkoutRepository) : ViewModel() {
    // var state = mutableStateOf(HomeState())
    val meals = mealsUseCases.getMeals(
        firstName = "a"
    ).cachedIn(viewModelScope)
    private val _daylyMeals = MutableStateFlow<List<DayMeal>>(emptyList())
    val daylyMeals: StateFlow<List<DayMeal>> = _daylyMeals.asStateFlow()

    init {
        loadMeals()

    }
    private fun loadMeals(){
        viewModelScope.launch {
            _daylyMeals.value = repository.loadMealPlan()
        }
    }
    private fun getWorkoutsByDay(){
        viewModelScope.launch {  }
    }
}

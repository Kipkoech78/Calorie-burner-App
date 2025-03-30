package com.fitnessapp.presentation.premiumApiFood

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitnessapp.domain.mealsUseCases.MealsUseCases
import com.fitnessapp.domain.repo.MealsRepository
import com.fitnessapp.utils.Event
import com.fitnessapp.utils.components.sendEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PremiumFoodViewModel @Inject constructor(
    private val foodUseCases: MealsUseCases,
    private val repository: MealsRepository
): ViewModel() {
    private val _state = MutableStateFlow(MealViewState())
    val state = _state.asStateFlow()
    init {
        getMeals()
    }

    fun getMeals(){
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }
            repository.getRandomMeal()
                .onRight { meal ->
                    _state.update {
                        it.copy(food = meal)
                    }
                    Log.d("mea;s ", "$meal")
                }
                .onLeft { error->
                    _state.update {
                        it.copy(
                            error=error.error.message

                        )

                    }
                    Log.d("message", error.error.message)
                    sendEvent(Event.Toast(error.error.message))
                }
            _state.update {
                it.copy( isLoading = false)
            }
        }
    }
}
package com.fitnessapp.presentation.premiumApiFood

import com.fitnessapp.models.FoodResponse
import com.fitnessapp.models.genModels.Data

data class MealViewState(
    val isLoading: Boolean = false,
    val food: List<Data> = emptyList(),
    val error: String? = null
)
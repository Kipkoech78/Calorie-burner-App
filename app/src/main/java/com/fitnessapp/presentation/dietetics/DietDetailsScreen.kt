package com.fitnessapp.presentation.dietetics

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fitnessapp.models.DayMeal

@Composable

fun DietDetailsScreen(mealContent: DayMeal,
                      navigateUp : ()-> Unit
    
) {
    Text(text = mealContent.meals.dinner)
}
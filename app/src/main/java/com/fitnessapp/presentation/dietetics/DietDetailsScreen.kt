package com.fitnessapp.presentation.dietetics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fitnessapp.models.DayMeal
import com.fitnessapp.models.Meals

@Composable

fun DietDetailsScreen(mealContent: Meals,
                      navigateUp : ()-> Unit
    
) {
    Column(
        modifier = Modifier.padding(20.dp).statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = mealContent.breakfast)
        Text(text = mealContent.secondBreakfast)
        Text(text = mealContent.lunch)
        Text(text = mealContent.afternoonSnack)
        Text(text = mealContent.dinner)

    }
}
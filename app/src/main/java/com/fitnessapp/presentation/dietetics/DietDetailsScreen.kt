package com.fitnessapp.presentation.dietetics

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.fitnessapp.R
import com.fitnessapp.models.DayMeal
import com.fitnessapp.models.Meals

@Composable

fun DietDetailsScreen(mealContent: Meals,
                      navigateUp : ()-> Unit
    
) {
    Column(
        modifier = Modifier
            .padding(20.dp).fillMaxSize().verticalScroll(rememberScrollState())
            .statusBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        FilledMealsCard(time = mealContent.breakfast.type, meal = mealContent.breakfast.whatToEat )
        Spacer(modifier = Modifier.height(10.dp))
        FilledMealsCard(time = mealContent.secondBreakfast.type, meal = mealContent.secondBreakfast.whatToEat )
        Spacer(modifier = Modifier.height(10.dp))
        FilledMealsCard(time = mealContent.lunch.type, meal = mealContent.lunch.whatToEat )
        Spacer(modifier = Modifier.height(10.dp))
        FilledMealsCard(time = mealContent.afternoonSnack.type, meal = mealContent.afternoonSnack.whatToEat )
        Spacer(modifier = Modifier.height(10.dp))
        FilledMealsCard(time = mealContent.dinner.type, meal = mealContent.dinner.whatToEat )
        Spacer(modifier = Modifier.height(10.dp))


//        Text(text = mealContent.breakfast.type)
//        Text(text = mealContent.breakfast.whatToEat)
//        Text(text = mealContent.secondBreakfast.type)
//        Text(text = mealContent.secondBreakfast.whatToEat)
//        Text(text = mealContent.lunch.type)
//        Text(text = mealContent.lunch.whatToEat)
//        Text(text = mealContent.afternoonSnack.type)
//        Text(text = mealContent.afternoonSnack.whatToEat)
//        Text(text = mealContent.dinner.type)
//        Text(text = mealContent.dinner.whatToEat)

    }
}
@Composable
fun FilledMealsCard(time: String, meal: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier.fillMaxWidth().height(200.dp)
    ) {
        Text(
            text = time,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = colorResource(id = R.color.text_title),

            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
        Text(
            text = meal,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
            color = colorResource(id = R.color.body),
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Start,
        )
    }
}
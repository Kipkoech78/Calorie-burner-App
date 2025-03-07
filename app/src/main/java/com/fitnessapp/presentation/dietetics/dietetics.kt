package com.fitnessapp.presentation.dietetics

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitnessapp.models.DayMeal
import com.google.gson.Gson

@Composable
fun DieteticsScreen(
    //meals: LazyPagingItems<Meal>,
    meals: State<List<DayMeal>>,
    viewModel: DieteticsViewModel = hiltViewModel(),
    navigateToDet: () -> Unit

                   // navigateToDetails: (Meal) -> Unit,
                    ) {
  //  val gridItems  = remember { mutableStateOf(viewModel.daylyMeals.value) }
    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
        Spacer(modifier = Modifier.height(10.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(meals.value){ meal ->
                DayCard(meal.day, onClick = {
                    navigateToDet()
                })

            }
        }


    }
}

@Composable
fun DayCard(day: String, onClick: ()-> Unit) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 240.dp, height = 200.dp)
            .clickable {
                onClick()
                }
    ) {
        Text(
            text = day,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center,
        )
    }

}

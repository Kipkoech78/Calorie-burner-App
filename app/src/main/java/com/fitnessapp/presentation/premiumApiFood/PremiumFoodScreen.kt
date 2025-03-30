package com.fitnessapp.presentation.premiumApiFood

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.fitnessapp.R
import com.fitnessapp.models.genModels.Data
import com.fitnessapp.presentation.dietetics.LoadingDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PremiumFoodScreen(
    navController: NavController,
    viewModel: PremiumFoodViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        TopAppBar(
            title = { Text("Your Today's Meal ") },
            modifier = Modifier.clickable {viewModel.getMeals() },
            navigationIcon = {
                IconButton(onClick = {navController.navigateUp()}) {
                    Image(painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription = null ,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                    )
                }
            },
            actions = {
                // RowScope here, so these icons will be placed horizontally
                IconButton(onClick = { viewModel.getMeals()}) {
                   Icon(painter = painterResource(id =  R.drawable.baseline_refresh_24), contentDescription = null)
                }
//                IconButton(onClick = { /* doSomething() */ }) {
//                    Icon(Icons.Filled.Favorite, contentDescription = "Localized description")
//                }
            }
        )
        Spacer(modifier = Modifier.height(15.dp))

        // Show Loading Dialog
        if (state.isLoading) {
            LoadingDialog(isLoading = true)
        }

        // Show Error Message if Available
        state.error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = Color.Red,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp)
            )
        }
        // Show Food List
        FoodsContent(state = state)
    }
}

@Composable
fun FoodsContent(state: MealViewState) {
    if (state.food.isEmpty() && !state.isLoading) {
        // Show No Data Message
        Text(
            text = "No food available.",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(state.food){ food ->
                MealCard(meal = food)

            }
        }
    }
}

@Composable
fun MealCard(meal: Data) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
        ),
        border = BorderStroke(1.dp, colorResource(id = R.color.text_title)),
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = meal.name, style = MaterialTheme.typography.headlineLarge,
                color = colorResource(id = R.color.text_title) )
            Text(text = "Category: ${meal.category}", style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.text_title)
            )
            Text(text = "Goal: ${meal.goal.joinToString(", ")}",
                color = colorResource(id = R.color.text_title),
                style = MaterialTheme.typography.bodyMedium)
            Text(text = "Fat: ${meal.fat}g", style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.text_title))
        }
    }
}
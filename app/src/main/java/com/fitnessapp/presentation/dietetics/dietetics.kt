package com.fitnessapp.presentation.dietetics

import android.net.Uri
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigator
import com.fitnessapp.R
import com.fitnessapp.models.DayMeal
import com.google.gson.Gson
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DieteticsScreen(
    //meals: LazyPagingItems<Meal>,
    meals: State<List<DayMeal>>,
    viewModel: DieteticsViewModel = hiltViewModel(),
    navigateToDet: NavController


                   // navigateToDetails: (Meal) -> Unit,
                    ) {
  //  val gridItems  = remember { mutableStateOf(viewModel.daylyMeals.value) }
    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {

        TopAppBar(
            title = { Text("Check Premium Food here  --> ") },
            modifier = Modifier.clickable { navigateToDet.navigate("PremiumFoodScreen") },
            navigationIcon = {
                IconButton(onClick = {navigateToDet.navigate("PremiumFoodScreen") }) {
                    Image(painter = painterResource(id = R.drawable.fried_chicken), contentDescription = null ,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                    )
                }
            },
            actions = {
                // RowScope here, so these icons will be placed horizontally
                IconButton(onClick = { /* doSomething() */ }) {
                    Image(painter = painterResource(id = R.drawable.lunchbox), contentDescription = null )
                }
//                IconButton(onClick = { /* doSomething() */ }) {
//                    Icon(Icons.Filled.Favorite, contentDescription = "Localized description")
//                }
            }
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(meals.value){ meals ->
                DayCard(day = meals.days.day, image = meals.days.image, onClick = {
                    val meal = Uri.encode(Gson().toJson(meals.meals))
                    navigateToDet.navigate("DietDetailsScreen/$meal")
                })

            }
        }
    }
}

@Composable
fun DayCard(day: String, image: String, onClick: ()-> Unit) {
    val context = LocalContext.current
    val imageId = context.resources.getIdentifier(
        image.substringBeforeLast("."), // Remove file extension
        "drawable",
        context.packageName
    )
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier
            .size(width = 220.dp, height = 200.dp)
            .clickable {
                onClick()
            },


    ) {
        Column(modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = day,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = colorResource(id = R.color.body),
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Image(painter = painterResource(id = imageId), contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .size(120.dp)
                )


        }

    }

}

package com.fitnessapp.presentation.navgraph

import android.content.Context
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fitnessapp.R
import com.fitnessapp.presentation.credentials.AddCredentialsScreen
import com.fitnessapp.presentation.credentials.SaveCredentialsViewModel
import com.fitnessapp.presentation.home.CardItems
import com.fitnessapp.presentation.home.HomeScreen
import com.fitnessapp.utils.Constants

@Composable
fun NavGraph(startDestination: String) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(Constants.U_PREF, Context.MODE_PRIVATE)
    val gender = sharedPreferences.getString(Constants.GENDER, "") ?: ""
    val navController = rememberNavController()

    val cardItem = listOf(
        CardItems(
            ratings = 4.5,
            gender = "male",
            desc = "Lose Belly Fat",
            image = R.drawable.abbsman
        ),
        CardItems(
            ratings = 4.5,
            gender = "male",
            desc = "Keep Fit",
            image = R.drawable.boyarmmuscles
        ),
        CardItems(
            ratings = 4.5,
            gender = "male",
            desc = "Six Pack Abs",
            image = R.drawable.sexymusculer
        ),

        CardItems(
            ratings = 4.5,
            gender = "male",
            desc = "Rock Hard Abs",
            image = R.drawable.abbsman
        ),

        CardItems(
            ratings = 4.5,
            gender = "female",
            desc = "Six Pack Abs",
            image = R.drawable.femaleabbs
        ),

        CardItems(
            ratings = 4.5,
            gender = "female",
            desc = "Keep Fit",
            image = R.drawable.closegirlfit
        ),
        CardItems(
            ratings = 4.5,
            gender = "female",
            desc = "Get Shaped",
            image = R.drawable.fitnessgirl
        ),
        CardItems(
            ratings = 4.5,
            gender = "female",
            desc = "Rock Hard Abs",
            image = R.drawable.womanabs1
        ),
    )
    NavHost(navController = navController, startDestination = startDestination , ){
        composable(Route.AddCredentialsScreen.route){
            val viewModel : SaveCredentialsViewModel = hiltViewModel()
            AddCredentialsScreen(event = viewModel::onEvent)
        }
        composable(Route.HomeScreen.route){
            val filterCards = cardItem.filter { it.gender.equals(gender, ignoreCase = true) }
            HomeScreen(cards = filterCards)
        }
        composable(Route.StartScreen.route){
            Text(text = " StartScreeen Endtersddeadev ")
        }
    }
}


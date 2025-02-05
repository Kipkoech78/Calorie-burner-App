package com.fitnessapp.presentation.navgraph

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fitnessapp.presentation.credentials.AddCredentialsScreen
import com.fitnessapp.presentation.credentials.SaveCredentialsViewModel
import com.fitnessapp.presentation.home.HomeScreen


@Composable
fun NavGraph(startDestination: String) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination , ){
        composable(Route.AddCredentialsScreen.route){
            val viewModel : SaveCredentialsViewModel = hiltViewModel()
            AddCredentialsScreen(event = viewModel::onEvent)

        }
        composable(Route.HomeScreen.route){
            HomeScreen()
        }
        composable(Route.StartScreen.route){
            Text(text = " StartScreeen Endtersddeadev ")
        }
    }
}
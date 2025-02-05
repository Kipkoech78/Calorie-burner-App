package com.fitnessapp.presentation.navgraph

sealed class Route(val route: String) {
    object AddCredentialsScreen:Route("AddCredentialsScreen")
    object HomeScreen: Route("HomeScreen")
    object DieteticsScreen: Route("dietetics")
    object  ProgressScreen: Route("ProgressScreen")
    object  StartScreen: Route("StartScreen")
}
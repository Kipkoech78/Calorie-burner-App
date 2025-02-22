package com.fitnessapp.presentation.navgraph

import android.content.Context
import android.util.Log
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fitnessapp.R
import com.fitnessapp.models.WorkoutVideo
import com.fitnessapp.presentation.credentials.AddCredentialsScreen
import com.fitnessapp.presentation.credentials.SaveCredentialsViewModel
import com.fitnessapp.presentation.dashBoard.DashboardScreen
import com.fitnessapp.presentation.dashBoard.WorkoutsProgressViewModel
import com.fitnessapp.presentation.home.CardItems
import com.fitnessapp.presentation.home.HomeScreen
import com.fitnessapp.presentation.workouts.WorkoutListScreen
import com.fitnessapp.presentation.workouts.WorkoutViewModel
import com.fitnessapp.presentation.workouts.WorkoutsDetailScreen
import com.fitnessapp.utils.Constants
import com.google.gson.Gson

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
            category = "lose_belly_fat",
            desc = "Lose Belly Fat",
            image = R.drawable.abbsman
        ),
        CardItems(
            ratings = 4.5,
            gender = "male",
            desc = "Keep Fit",
            category = "keep_fit",
            image = R.drawable.boyarmmuscles
        ),
        CardItems(
            ratings = 4.5,
            gender = "male",
            desc = "Six Pack Abs",
            category = "six_pack_abs",
            image = R.drawable.sexymusculer
        ),

        CardItems(
            ratings = 4.5,
            gender = "male",
            desc = "Rock Hard Abs",
            category = "rock_hard_abs",
            image = R.drawable.abbsman
        ),

        CardItems(
            ratings = 4.5,
            gender = "female",
            desc = "Six Pack Abs",
            category = "six_pack_abs",
            image = R.drawable.femaleabbs
        ),

        CardItems(
            ratings = 4.5,
            gender = "female",
            category = "keep_fit",
            desc = "Keep Fit",
            image = R.drawable.closegirlfit
        ),
        CardItems(
            ratings = 4.5,
            gender = "female",
            desc = "Get Shaped",
            category = "get_shaped",
            image = R.drawable.fitnessgirl
        ),
        CardItems(
            ratings = 4.5,
            gender = "female",
            desc = "Rock Hard Abs",
            category = "six_pack_abs",
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
            HomeScreen(navController = navController, cards = filterCards,
//                navigateToList = { category ->
//                    navigateToList(navController = navController, category = category)
//                }
            )
        }
        composable(Route.StartScreen.route){
            Text(text = " StartScreeen Endtersddeadev ")
        }
//        composable(Route.WorkoutListScreen.route){
//                fun getCategory(): String? {
//                    return navController.previousBackStackEntry?.savedStateHandle?.get<String>("category")
//            }
//            WorkoutListScreen(category = getCategory().toString(),
//                navigateUp = {navController.navigateUp()},
//                navigateToDet ={video ->
//                    navigateToDetails(navController, video)
//                })
//        }
        //
        composable("WorkoutListScreen/{category}" ) { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category")?:""
            // val viewModel: com.fitnessapp.presentation.workouts.WorkoutViewModel = hiltViewModel()
            WorkoutListScreen(category = category, navController = navController, navigateUp = {
                navController.navigateUp()
            } )

        }

        //
        composable("WorkoutDetailScreen/{video}"){ backStackEntry ->
            val videoResId = backStackEntry.arguments?.getString("video")
            val viewModel: WorkoutViewModel = hiltViewModel()
            val videoJson = backStackEntry.arguments?.getString("video")
            val video = videoJson?.let { Gson().fromJson(it, WorkoutVideo::class.java) }
            Log.d("videos at navGraph", "$video")
            WorkoutsDetailScreen(progress =video!!,
                navigateUp = { navController.navigateUp() },
                event = viewModel::onEvent
            )
        }
//        composable(Route.WorkoutDetailScreen.route){
//            val viewModel: WorkoutViewModel = hiltViewModel()
//            Log.d("sideEffects", "${viewModel.sideEffect}")
//            if(viewModel.sideEffect != null){
//                Toast.makeText(LocalContext.current, viewModel.sideEffect, Toast.LENGTH_SHORT).show()
//                viewModel.onEvent(SaveProgressEvent.RemoveSideEffect)
//            }
//                navController.currentBackStackEntry?.savedStateHandle?.get<WorkoutVideo>("workout_progress")?.let {
//                    progress ->
//                    Log.d("progress", "$progress")
//                    WorkoutsDetailScreen(progress = progress,
//                       event = viewModel::onEvent,
//                        navigateUp  = {navController.navigateUp()}
//                        )
//                }
//        }
        composable(Route.WorkoutsScreen.route){
           //  WorkoutVideoScreen()
        }
        composable(Route.ProgressScreen.route){
            val viewModel: WorkoutsProgressViewModel = hiltViewModel()
            DashboardScreen( viewModel )
        }
    }
}
//passing object
private fun navigateToDetails(navController: NavController, video: WorkoutVideo){
    Log.d("Navigation", "Setting videos: $video") // ✅
    navController.currentBackStackEntry?.savedStateHandle?.set("workout_progress", video)
    navController.navigate(
        Route.WorkoutDetailScreen.route
    )
}
private fun navigateToList(navController: NavController, category: String) {
    Log.d("Navigation", "Setting category: $category") // ✅ Log before setting
    navController.currentBackStackEntry?.savedStateHandle?.set("category", category)
    navController.navigate(Route.WorkoutListScreen.route)
}




//        composable("WorkoutDetailScreen/{video}"){ backStackEntry ->
//            val videoResId = backStackEntry.arguments?.getString("video")
//            val videoJson = backStackEntry.arguments?.getString("video")
//            val video = videoJson?.let { Gson().fromJson(it, WorkoutVideo::class.java) }
//            Log.d("videos at navGraph", "$video")
//            WorkoutDetailScreen(videoResId =video!!,
//                navController = navController,
//                event = viewModel::onEvent
//            )
//        }
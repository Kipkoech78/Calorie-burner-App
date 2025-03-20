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
import com.fitnessapp.presentation.FitnessBaseNavigation.FitnessNavigator
import com.fitnessapp.presentation.credentials.AddCredentialsScreen
import com.fitnessapp.presentation.credentials.SaveCredentialsViewModel


@Composable
fun NavGraph(startDestination: String) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination , ){
        composable(Route.AddCredentialsScreen.route){
            val viewModel : SaveCredentialsViewModel = hiltViewModel()
            AddCredentialsScreen(event = viewModel::onEvent)
        }
        composable(Route.FitnessNavigatorScreen.route){
            FitnessNavigator()
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


    }
}
//passing object
//private fun navigateToDetails(navController: NavController, video: WorkoutVideo){
//    Log.d("Navigation", "Setting videos: $video") // ✅
//    navController.currentBackStackEntry?.savedStateHandle?.set("workout_progress", video)
//    navController.navigate(
//        Route.WorkoutDetailScreen.route
//    )
//}
//private fun navigateToList(navController: NavController, category: String) {
//    Log.d("Navigation", "Setting category: $category") // ✅ Log before setting
//    navController.currentBackStackEntry?.savedStateHandle?.set("category", category)
//    navController.navigate(Route.WorkoutListScreen.route)
//}




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
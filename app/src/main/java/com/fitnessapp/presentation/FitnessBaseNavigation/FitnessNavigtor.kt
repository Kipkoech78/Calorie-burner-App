package com.fitnessapp.presentation.FitnessBaseNavigation

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.paging.compose.collectAsLazyPagingItems
import com.fitnessapp.R
import com.fitnessapp.models.WorkoutVideo
import com.fitnessapp.presentation.dietetics.DieteticsScreen
import com.fitnessapp.presentation.dashBoard.DashboardScreen
import com.fitnessapp.presentation.dashBoard.WorkoutsProgressViewModel
import com.fitnessapp.presentation.dietetics.DieteticsViewModel
import com.fitnessapp.presentation.home.CardItems
import com.fitnessapp.presentation.home.HomeScreen
import com.fitnessapp.presentation.navgraph.Route
import com.fitnessapp.presentation.workouts.WorkoutListScreen
import com.fitnessapp.presentation.workouts.WorkoutViewModel
import com.fitnessapp.presentation.workouts.WorkoutsDetailScreen
import com.fitnessapp.utils.Constants
import com.google.gson.Gson

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FitnessNavigator() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(Constants.U_PREF, Context.MODE_PRIVATE)
    val gender = sharedPreferences.getString(Constants.GENDER, "") ?: ""
    val BottomNavigationItem = remember {
        listOf(
            BottomNavItems(icon = R.drawable.ic_home, text = "Home"),
            BottomNavItems(icon = R.drawable.ic_self_improve, text = "Dietetics"),
            BottomNavItems(icon = R.drawable.ic_preferences, text = "Progress")
        )
    }
    val navController = rememberNavController()
    val backStackState = navController.currentBackStackEntryAsState().value
    var selectedItem by rememberSaveable { mutableIntStateOf(0)}
    selectedItem = remember(key1 = backStackState) {
        when(backStackState?.destination?.route){
            Route.HomeScreen.route -> 0
            Route.DieteticsScreen.route -> 1
            Route.ProgressScreen.route -> 2
            else -> 0
        }
    }
    val isNavBarVisible = remember(key1 = backStackState) {
        backStackState?.destination?.route == Route.HomeScreen.route || backStackState?.destination?.route == Route.DieteticsScreen.route
                ||backStackState?.destination?.route ==Route.ProgressScreen.route
    }
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
    Scaffold(modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if(isNavBarVisible){
                FitnessBottomNavigation(items = BottomNavigationItem , selected = selectedItem,
                    onItemClicked = {index ->
                        when(index){
                            0->{
                                navigateToTap(navController = navController, route = Route.HomeScreen.route)
                            }
                            1 ->{
                                navigateToTap(navController = navController, route = Route.DieteticsScreen.route)
                            }
                            2-> {
                                navigateToTap(navController = navController, route = Route.ProgressScreen.route)
                            }
                        }
                    }
                )
            }
        }
        )
    {
        NavHost(navController = navController, startDestination = Route.HomeScreen.route,
            modifier = Modifier.padding(bottom = 8.dp)) {
            composable(Route.HomeScreen.route){
                val filterCards = cardItem.filter { it.gender.equals(gender, ignoreCase = true) }
                HomeScreen(navController = navController, cards = filterCards,
                )
            }
            composable("WorkoutListScreen/{category}" ) { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category")?:""
                // val viewModel: com.fitnessapp.presentation.workouts.WorkoutViewModel = hiltViewModel()
                WorkoutListScreen(category = category, navController = navController, navigateUp = {
                    navController.navigate(Route.HomeScreen.route){
                        popUpTo(Route.WorkoutListScreen.route){
                            inclusive = true
                            saveState = true
                        }
                    }
                } )

            }
            composable(Route.DieteticsScreen.route){
                val viewModel: DieteticsViewModel = hiltViewModel()
                val meals = viewModel.meals.collectAsLazyPagingItems()
                DieteticsScreen(meals = meals)
            }
            //
            composable("WorkoutDetailScreen/{video}"){ backStackEntry ->
                val videoResId = backStackEntry.arguments?.getString("video")
                val viewModel: WorkoutViewModel = hiltViewModel()
                val videoJson = backStackEntry.arguments?.getString("video")
                val video = videoJson?.let { Gson().fromJson(it, WorkoutVideo::class.java) }
                Log.d("videos at navGraph", "$video")
                WorkoutsDetailScreen(progress =video!!,
                    navigateUp = { navController.navigateUp()
                    },
                    event = viewModel::onEvent
                )
            }
            composable(Route.ProgressScreen.route){
                val viewModel: WorkoutsProgressViewModel = hiltViewModel()
                DashboardScreen( viewModel )
            }
        }
    }
}
fun navigateToTap(navController: NavController, route: String) {
    navController.navigate(route){
        navController.graph.startDestinationRoute?.let { homeScreen ->
            popUpTo(homeScreen){
                saveState = true
            }
            restoreState = true
            launchSingleTop = true
        }
    }
}
@Composable
fun FitnessBottomNavigation(items: List<BottomNavItems>,
                         selected: Int,
                         onItemClicked: (Int) -> Unit,
) {
    NavigationBar(modifier = Modifier.fillMaxWidth(),
        containerColor = MaterialTheme.colorScheme.background,
        tonalElevation = 3.dp) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(selected = index == selected ,
                onClick = { onItemClicked(index) },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(painter = painterResource(id = item.icon), contentDescription = null,
                            modifier = Modifier.size(30.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = item.text, style = MaterialTheme.typography.labelSmall)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedIconColor = colorResource(id = R.color.body),
                    unselectedTextColor = colorResource(id = R.color.body),
                    indicatorColor = MaterialTheme.colorScheme.background
                )
            )
        }
    }
}
data class BottomNavItems(
    @DrawableRes val icon: Int,
    val text: String
)
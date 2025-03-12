package com.fitnessapp.presentation.workouts
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitnessapp.R
import com.fitnessapp.presentation.EmptyScreen
import com.google.gson.Gson

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutListScreen(
    viewModel: WorkoutViewModel = hiltViewModel(),
    category: String,
    gender: String,
    navigateUp: ()-> Unit,
    navController: NavController
    // navigateToDet: (WorkoutVideo) -> Unit
) {
    val filteredWorkouts by viewModel.filteredWorkouts.collectAsState()
    LaunchedEffect(category) {
        viewModel.filterWorkoutsByCategory(category, gender)
    }
    if(filteredWorkouts.isNotEmpty()){
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .background(color = colorResource(id = R.color.fit_background))

        ) {
            TopAppBar(title = {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription = "",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(50.dp))
                            .padding(end = 20.dp)
                            .clickable { navigateUp() }
                    )
                    Text(text = "Composure,, Lets keep fit Today.",modifier = Modifier.weight(0.8f),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        color = colorResource(id = R.color.text_title),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                }
             })
            LazyColumn(

            ) {
                items(filteredWorkouts){video ->
           // filteredWorkouts.forEach { video ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(120.dp)
                        .padding(8.dp)
                        .clickable {
                            val videoJson = Uri.encode(Gson().toJson(video))
                            val videoListJson = Uri.encode(Gson().toJson(filteredWorkouts))
                            navController.navigate("WorkoutDetailScreen/$videoJson?videoList=$videoListJson")

                        },
                    elevation = CardDefaults.cardElevation(50.dp) ,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                            WorkoutsVideoPlayer(uri = video.videoResId)

                    Column(
                        modifier = Modifier
                            .weight(0.7f)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = video.name,
                            fontSize = 20.sp,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                           color = colorResource(id = R.color.text_title),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = video.description,
                            fontSize = 13.sp,
                            color = colorResource(id = R.color.body)
                        )
                    }
                }
                }
           // }
                }
            }
        }
    }else{
        Spacer(modifier = Modifier.height(100.dp))
        EmptyScreen()
    }

}


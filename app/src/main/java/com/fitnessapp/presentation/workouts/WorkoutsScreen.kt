package com.fitnessapp.presentation.workouts
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import android.widget.VideoView
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.fitnessapp.R
import com.fitnessapp.models.WorkoutVideo
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
@Composable
fun WorkoutVideoScreen() {
    val context = LocalContext.current
    val workoutVideos = remember { loadWorkoutVideos(context) }
    var currentVideoIndex by remember { mutableStateOf(0) }
    val videoView = remember { VideoView(context) }
    val handler = remember { Handler(Looper.getMainLooper()) }

    if (workoutVideos.isNotEmpty()) {
        val currentVideo = workoutVideos[currentVideoIndex]
        LaunchedEffect(currentVideoIndex) {
            val videoUri = Uri.parse("android.resource://${context.packageName}/raw/${currentVideo.videoResId}")
            videoView.setVideoURI(videoUri)
            videoView.setOnCompletionListener { videoView.start() }  // Loop video
            videoView.start()
            // Stop after 30 seconds
            handler.postDelayed({
                Toast.makeText(context, "Workout Complete!", Toast.LENGTH_SHORT).show()
            }, 30_000L)
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Workout: ${currentVideo.category}",
                fontSize = 20.sp,

                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.text_title),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            AndroidView(

                factory = { videoView },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )
            Button(
                onClick = {
                    currentVideoIndex = (currentVideoIndex + 1) % workoutVideos.size
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Next Video")
            }
        }
    } else {
        Text("No workouts available", Modifier.fillMaxSize(), textAlign = TextAlign.Center)
    }
}
fun loadWorkoutVideos(context: Context): List<WorkoutVideo> {
    return try {
        val json = context.assets.open("workouts.json").bufferedReader().use { it.readText() }
        val type = object : TypeToken<List<WorkoutVideo>>() {}.type
        Gson().fromJson(json, type)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutListScreen(
    viewModel: WorkoutViewModel = hiltViewModel(),
    category: String,
    navController: NavController) {
    val context = LocalContext.current
    val filteredWorkouts by viewModel.filteredWorkouts.collectAsState()
    LaunchedEffect(category) {
        viewModel.filterWorkoutsByCategory(category)
    }
    if(filteredWorkouts.isNotEmpty()){
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            TopAppBar(title = {
                Row(modifier = Modifier.fillMaxWidth().padding(end = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription = "",
                        modifier = Modifier.size(50.dp).clip(RoundedCornerShape(50.dp)).padding(end = 20.dp)
                            .clickable { navController.navigateUp() }
                    )
                    Text(text = "Composure,, Lets keep fit Today.",modifier = Modifier.weight(0.8f),
                        textAlign = TextAlign.Center,
                        fontSize = 24.sp,
                        color = colorResource(id = R.color.text_title),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
                        )
                }
             })
            filteredWorkouts.forEach { video ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(120.dp)
                        .padding(8.dp)
                        .clickable {
                            val videoJson = Uri.encode(Gson().toJson(video))
                            navController.navigate("WorkoutDetailScreen/$videoJson")
                        },
                    elevation = CardDefaults.cardElevation(50.dp) ,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier,
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier
                            .width(120.dp)
                            .weight(0.4f)) {
                            WorkoutsVideoPlayer(uri = video.videoResId)
                        }
                    Column(
                        modifier = Modifier
                            .weight(0.6f)
                            .padding(16.dp)
                    ) {
                        Text(
                            text = video.videoResId,
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
            }
        }
    }else{
        Spacer(modifier = Modifier.height(100.dp))
        Text(text =" Null Values", color = colorResource(id = R.color.text_title))
    }

}


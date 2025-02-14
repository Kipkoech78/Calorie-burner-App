package com.fitnessapp.presentation.workouts
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import android.widget.VideoView
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
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

@Composable
fun WorkoutDetailScreen(
    videoResId: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val videoView = remember { VideoView(context) }

    LaunchedEffect(videoResId) {
        val videoUri = Uri.parse("android.resource://${context.packageName}/$videoResId")
        videoView.setVideoURI(videoUri)
        videoView.setOnCompletionListener { videoView.start() }  // Loop video
        videoView.start()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { onBack() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text("Back")
        }

        AndroidView(
            factory = { videoView },
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
    }
}
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

//
//    val allVideos = remember { loadWorkoutVideos(context) }
//    val filteredVideos = remember(gender, category) {
//        allVideos.filter {
//            it.gender.equals(gender , ignoreCase = true)
//        }
//    }
    Log.d("gender and cat", "${filteredWorkouts}")
    if(filteredWorkouts.isNotEmpty()){
        Column(
            modifier = Modifier
                .statusBarsPadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            filteredWorkouts.forEach { video ->
                val videoResId = video.videoResId
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(120.dp)
                        .padding(8.dp)
                        .clickable {

                            navController.navigate("WorkoutDetailScreen/${videoResId}")
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
                            text = video.category,
                            fontSize = 20.sp,
                           color = colorResource(id = R.color.body),
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = video.gender,
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = video.description,
                            fontSize = 14.sp,
                            color = Color.DarkGray
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


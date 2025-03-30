package com.fitnessapp.presentation.workouts
import android.net.Uri
import android.util.Log
import androidx.annotation.OptIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
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
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.ui.PlayerView
import androidx.navigation.NavController
import com.fitnessapp.R
import com.fitnessapp.models.WorkoutVideo
import com.fitnessapp.models.WorkoutsProgress
import com.fitnessapp.presentation.dietetics.ShimmerEffect
import com.fitnessapp.presentation.dietetics.shimmerEffect
import com.fitnessapp.presentation.navgraph.Route
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
@OptIn(UnstableApi::class)
@kotlin.OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutsDetailScreen(
    event: (SaveProgressEvent) -> Unit,
    progress: WorkoutVideo?,
    navigateUp: () -> Unit,
    navController: NavController
) {
    val context = LocalContext.current
    var timeLeft by remember { mutableStateOf(60) } // 1-minute timer
    val coroutineScope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }
    var hasSaved by remember { mutableStateOf(false) }
    var isEnabled by remember{ mutableStateOf(false)}
    var isPlaying by remember { mutableStateOf(true) }
    var timerJob by remember{ mutableStateOf<Job?>(null)}
    val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    // Initialize the ExoPlayer
    val player = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
        }
    }
    // Initialize the PlayerView
    val playerView = remember {
        PlayerView(context).apply {
            useController = false
        }
    }
    fun saveProgress() {
        if ( !hasSaved) {
         //   event(SaveProgressEvent.UpdateProgress(date = date, duration = 60 - timeLeft))
            event(SaveProgressEvent.UpsertProgress(WorkoutsProgress(date = date, duration = 60-timeLeft)))
            Log.d("WorkoutProgress", "Saving progress on exit: date=$date, duration=$timeLeft")
            hasSaved = true
            navigateUp()
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.fit_background))
        .statusBarsPadding()
    ) {
        TopAppBar(
            title = {
                progress?.category?.let {
                    Text(text = it,
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .weight(0.8f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = colorResource(id = R.color.text_title)
                    )
                }
            },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Image(painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription = null ,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { saveProgress() }
                            .clip(CircleShape)
                    )
                }
            },

        )
        Spacer(modifier = Modifier.height(12.dp))
        progress?.videoResId?.let {
            Column(modifier = Modifier
                .fillMaxWidth()
                .weight(0.9f)
                .clip(RoundedCornerShape(20.dp))) {
                // Parse the video URI
                val videoUrl = Uri.parse("android.resource://${context.packageName}/raw/${progress.videoResId}")
                val mediaItem = MediaItem.fromUri(videoUrl)
                fun startTimer() {
                    timerJob?.cancel() // Cancel previous timer if exists
                    timerJob = coroutineScope.launch {
                        while (timeLeft > 0) {
                            delay(1000L)
                            timeLeft --
                        }
                        player.playWhenReady = false // Stop video when timer ends
                        isPlaying = false
                    }
                }
                // Start the video and timer when the Composable is active
                LaunchedEffect(Unit) {
                    player.setMediaItem(mediaItem)
                    player.prepare()
                    player.addListener(object : Player.Listener{
                        override fun onPlaybackStateChanged(state:Int){
                            if(state == Player.STATE_READY){
                                isLoading = false
                            }
                        }
                    })
                    player.playWhenReady = false // Start video

                }
                DisposableEffect(Unit) {
                    onDispose {
                        player.playWhenReady = false
                        player.stop()
                        player.release()
                        timerJob?.cancel()
                        saveProgress()
                    }
                }
                Column(
                    modifier = Modifier
                        //.clip(RoundedCornerShape(20.dp))
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    if(isLoading){
                        Box(
                            modifier = Modifier
                                .height(500.dp)
                                .fillMaxWidth()
                                .clip(MaterialTheme.shapes.medium)
                                .shimmerEffect()
                        )
                    }else{
                        // Display the PlayerView
                        AndroidView(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .fillMaxWidth(),

                            factory = { playerView.apply { this.player = player } }
                        )
                        // Timer display
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp),
                        ){
                            Text(
                                modifier = Modifier.weight(0.7f),
                                text = "Timer: ${timeLeft}s",
                                fontSize = 60.sp,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = colorResource(id = R.color.input_background))
                            if(isPlaying && timeLeft < 300){
                                isEnabled = true
                            }
                            if(timeLeft == 60){
                                Button(
                                    modifier = Modifier.padding(5.dp),
                                    onClick = {
                                        timeLeft = 60 // Reset timer
                                        player.seekTo(0) // Restart video
                                        player.playWhenReady = true
//                                        isPlaying = true
                                        startTimer() // Restart timer
                                    }
                                ) {
                                    Text("Start")
                                }
                            }else{
                                Button(
                                    enabled = isEnabled,
                                    onClick = {

                                        if (isPlaying) {
                                            player.playWhenReady = false
                                            isPlaying = false
                                            timerJob?.cancel() // Pause timer
                                        } else {
                                            player.playWhenReady = true
                                            isPlaying = true
                                            startTimer() // Resume timer
                                        }
                                    },
                                    ) {
                                    Text(if (isPlaying) "Stop" else "Continue")
                                }
                            }
                        }
                        Button(
                            modifier = Modifier.padding(5.dp),
                            onClick = {
                                timeLeft = 60 // Reset timer
                                player.seekTo(0) // Restart video
                                player.playWhenReady = true
                                isPlaying = true
                                startTimer() // Restart timer
                            }
                        ) {
                            Text("Restart")
                        }

                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = progress.description,
                        modifier = Modifier.padding(horizontal = 10.dp),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Start,
                        color = colorResource(id = R.color.text_title))
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(0.6f)) {
                            ExtendedFloatingActionButton(
                                onClick = {event(SaveProgressEvent.InsertFavWorkouts(WorkoutVideo(
                                    videoResId = progress.videoResId ,
                                    name = progress.name,
                                    category = progress.category,
                                    description = progress.description,
                                    gender = progress.gender
                                ))) },
                                icon = { Icon(Icons.Outlined.Favorite,
                                    "Extended floating action button.") },
                                text = { Text(text = "Add To Favourite") },
                            )
                        }
                        ExtendedFloatingActionButton(
                            modifier = Modifier
                                .weight(0.3f)
                                .padding(end = 5.dp),
                            onClick = { saveProgress() },
                            text = { Text(
                                text = "Close ",
                                color = colorResource(id = R.color.text_title),
                                fontSize = 23.sp,
                                fontWeight = FontWeight.Bold,
                            ) },
                            icon = { Icon(Icons.Filled.ExitToApp, "Extended floating action button.") },
                        )
                    }
                }
            }
        } ?: Text("Error: Video not found")

    }
}

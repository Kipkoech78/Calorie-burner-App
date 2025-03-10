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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.fitnessapp.R
import com.fitnessapp.models.WorkoutVideo
import com.fitnessapp.models.WorkoutsProgress
import com.fitnessapp.presentation.dietetics.ShimmerEffect
import com.fitnessapp.presentation.dietetics.shimmerEffect
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(UnstableApi::class)
@Composable
fun WorkoutsDetailScreen(
    event: (SaveProgressEvent) -> Unit,
    progress: WorkoutVideo?,
    navigateUp: () -> Unit
) {
    val context = LocalContext.current
    var timeLeft by remember { mutableStateOf(0) } // 1-minute timer
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
        if (timeLeft > 0 && !hasSaved) {
            hasSaved = true
            event(SaveProgressEvent.UpdateProgress(date = date, duration = timeLeft))
            event(SaveProgressEvent.UpsertProgress(WorkoutsProgress(date = date, duration = timeLeft)))
            Log.d("WorkoutProgress", "Saving progress on exit: date=$date, duration=$timeLeft")
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .background(color = colorResource(id = R.color.fit_background))
        .padding(top = 10.dp)
        .statusBarsPadding()
    ) {
        Spacer(modifier = Modifier.height(18.dp))
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription ="",
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .padding(5.dp)
                    .size(30.dp)
                    .clickable {
                        saveProgress()
                        navigateUp()
                    })
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
        }
        Spacer(modifier = Modifier.height(20.dp))
        progress?.videoResId?.let {
            Column(modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))) {


                // Parse the video URI
                val videoUrl = Uri.parse("android.resource://${context.packageName}/raw/${progress.videoResId}")
                val mediaItem = MediaItem.fromUri(videoUrl)
                fun startTimer() {
                    timerJob?.cancel() // Cancel previous timer if exists
                    timerJob = coroutineScope.launch {
                        while (timeLeft < 300) {
                            delay(1000L)
                            timeLeft ++
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
                                .height(400.dp).fillMaxWidth()
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
//                        Row(modifier = Modifier,
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.SpaceBetween) {
                            if(timeLeft == 0){
                                Button(
                                    modifier = Modifier.padding(5.dp),
                                    onClick = {
                                        timeLeft = 0 // Reset timer
                                        player.seekTo(0) // Restart video
                                        player.playWhenReady = true
//                                        isPlaying = true
                                        startTimer() // Restart timer
                                    }
                                ) {
                                    Text("Start",)
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

                            //   }
                        }
//                        if(!isPlaying && timeLeft != 0 ){
//                            event(SaveProgressEvent.UpdateProgress(date = date, duration = 60 - timeLeft))
//                            event(SaveProgressEvent.UpsertProgress(WorkoutsProgress(date = date, duration = (timeLeft))))
//                            Log.d("WorkoutProgress", "Saving progress: date=$date, duration=$timeLeft")
//
//                        }
                        Button(
                            modifier = Modifier.padding(5.dp),
                            onClick = {
                                timeLeft = 300 // Reset timer
                                player.seekTo(0) // Restart video
                                player.playWhenReady = true
                                isPlaying = true
                                startTimer() // Restart timer
                            }
                        ) {
                            Text("Restart",)
                        }

                    }

                }
            }
        } ?: Text("Error: Video not found")
    }
}
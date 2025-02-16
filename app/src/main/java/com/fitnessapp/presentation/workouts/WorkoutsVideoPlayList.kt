package com.fitnessapp.presentation.workouts

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.fitnessapp.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment

@OptIn(UnstableApi::class)
@Composable
fun WorkoutsVideoPlayer(uri: String) {
    val context = LocalContext.current
    // Initialize the ExoPlayer
    val player = remember {
        SimpleExoPlayer.Builder(context).build().apply {
            repeatMode = Player.REPEAT_MODE_ALL
        }
    }

    // Initialize the PlayerView
    val playerView = remember {
        PlayerView(context).apply {
            useController = false // Hide the controls (pause button, progress bar, etc.)
        }
    }
    // Parse the video URI
    val videoUrl = Uri.parse("android.resource://${context.packageName}/raw/${uri}")
    val mediaItem = MediaItem.fromUri(videoUrl)
    // Autoplay the video when the Composable is active
    LaunchedEffect(player) {
        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true // Autoplay the video
    }

    // Release the player when the Composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            player.release() // Release resources when the Composable leaves the screen
        }
    }
    // Display the PlayerView
    AndroidView(
        modifier = Modifier.size(120.dp),
        factory = { playerView.apply { this.player = player } }
    )
}
@OptIn(UnstableApi::class)
@Composable
fun WorkoutsVideoDetailPlayer(uri: String,) {
    val context = LocalContext.current
    var timeLeft by remember { mutableStateOf(60) } // 1-minute timer
    val coroutineScope = rememberCoroutineScope()
    var isPlaying by remember { mutableStateOf(true) }
    var timerJob by remember{ mutableStateOf<Job?>(null)}

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

    // Parse the video URI
    val videoUrl = Uri.parse("android.resource://${context.packageName}/raw/${uri}")
    val mediaItem = MediaItem.fromUri(videoUrl)
    fun startTimer() {
        timerJob?.cancel() // Cancel previous timer if exists
        timerJob = coroutineScope.launch {
            while (timeLeft > 0) {
                delay(1000L)
                timeLeft--
            }
            player.playWhenReady = false // Stop video when timer ends
            isPlaying = false
        }
    }
    // Start the video and timer when the Composable is active
    LaunchedEffect(Unit) {

        player.setMediaItem(mediaItem)
        player.prepare()
        player.playWhenReady = true // Start video
        startTimer()
    }

    // Release the player when the Composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            player.release()
            timerJob?.cancel()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth().padding(horizontal = 13.dp),
        horizontalAlignment = Alignment.Start
    ) {
        // Display the PlayerView
        AndroidView(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            factory = { playerView.apply { this.player = player } }
        )
        // Timer display
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth(),
            ){
            Text(
                text = "Time left: ${timeLeft}s",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = colorResource(id = R.color.input_background)

            )
            Button(
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
                }
            ) {
                Text(if (isPlaying) "Stop" else "Continue")
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
            Text("Restart",)
        }

    }
}

//
//@OptIn(UnstableApi::class)
//@Composable
//fun WorkoutsVideoDetailPlayer(uri: String) {
//    val context = LocalContext.current
//    // Initialize the ExoPlayer
//    val player = remember {
//        SimpleExoPlayer.Builder(context).build().apply {
//            repeatMode = Player.REPEAT_MODE_ALL
//
//        }
//    }
//
//    // Initialize the PlayerView
//    val playerView = remember {
//        PlayerView(context).apply {
//            useController = true // Hide the controls (pause button, progress bar, etc.)
//        }
//    }
//    // Parse the video URI
//    val videoUrl = Uri.parse("android.resource://${context.packageName}/raw/${uri}")
//    val mediaItem = MediaItem.fromUri(videoUrl)
//    // Autoplay the video when the Composable is active
//    LaunchedEffect(player) {
//        player.setMediaItem(mediaItem)
//        player.prepare()
//        player.playWhenReady = true // Autoplay the video
//    }
//
//    // Release the player when the Composable is disposed
//    DisposableEffect(Unit) {
//        onDispose {
//            player.release() // Release resources when the Composable leaves the screen
//        }
//    }
//    // Display the PlayerView
//    AndroidView(
//        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).height(300.dp),
//        factory = { playerView.apply { this.player = player } }
//    )
//}
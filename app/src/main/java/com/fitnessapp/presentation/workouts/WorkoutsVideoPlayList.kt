package com.fitnessapp.presentation.workouts

import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import android.widget.VideoView
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView

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
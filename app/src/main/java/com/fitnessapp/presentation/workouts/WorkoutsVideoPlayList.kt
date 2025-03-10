package com.fitnessapp.presentation.workouts

import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.SimpleExoPlayer
import androidx.media3.ui.PlayerView
import com.fitnessapp.R
import com.fitnessapp.presentation.dietetics.ShimmerEffect
import com.fitnessapp.presentation.dietetics.shimmerEffect
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(UnstableApi::class)
@Composable
fun WorkoutsVideoPlayer(uri: String) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
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
        player.addListener(object : Player.Listener{
            override fun onPlaybackStateChanged(state: Int) {
                if(state == Player.STATE_READY){
                    isLoading = false
                }
            }
        })
        player.playWhenReady = true // Autoplay the video
    }

    // Release the player when the Composable is disposed
    DisposableEffect(Unit) {
        onDispose {
            player.playWhenReady = false
            player.stop()
            player.release() // Release resources when the Composable leaves the screen
        }
    }
    // Display the PlayerView
    if(isLoading){
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(MaterialTheme.shapes.medium)
                .shimmerEffect()
        )
    }else{
        AndroidView(
            modifier = Modifier
                .size(160.dp)
                .clip(RoundedCornerShape(20.dp)),
            factory = { playerView.apply { this.player = player } }
        )
    }

}


package com.fitnessapp.presentation.workouts


import android.net.Uri
import android.util.Log
import android.widget.VideoView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.fitnessapp.R
import com.fitnessapp.models.WorkoutVideo

@Composable
fun WorkoutDetailScreen(
    videoResId: WorkoutVideo?
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .statusBarsPadding()

    ) {
        Text(text = videoResId?.category.toString(),
            modifier = Modifier.padding(horizontal = 20.dp),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = colorResource(id = R.color.text_title)
            )
        videoResId?.let {
            Column {
                WorkoutsVideoDetailPlayer(uri = it.videoResId)
            }
        } ?: Text("Error: Video not found")
    }
}

package com.fitnessapp.presentation.workouts
import android.net.Uri
import android.util.Log
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import com.fitnessapp.R
import com.fitnessapp.models.WorkoutVideo
@Composable
fun WorkoutDetailScreen(
    navController: NavController,
    videoResId: WorkoutVideo?
) {
    Column(modifier = Modifier
        .fillMaxSize().padding(top = 10.dp)
        .statusBarsPadding()
    ) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
            ) {
            Image(painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription ="",
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp)).padding(5.dp)
                    .size(30.dp)
                    .clickable { navController.navigateUp() })
            Text(text = videoResId?.category.toString(),
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .weight(0.8f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = colorResource(id = R.color.text_title)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        videoResId?.let {
            Column(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(20.dp))) {
                WorkoutsVideoDetailPlayer(uri = it.videoResId)
            }
        } ?: Text("Error: Video not found")
    }
}

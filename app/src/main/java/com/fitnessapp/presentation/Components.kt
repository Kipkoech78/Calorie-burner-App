package com.fitnessapp.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitnessapp.R
import com.fitnessapp.models.WorkoutVideo
import com.fitnessapp.presentation.workouts.loadWorkoutVideos
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeCard(
    text: String,
    rating: Double,
    onClick: ()-> Unit,
    imageVector: Painter,
) {
    val scope = rememberCoroutineScope() // Create a coroutine scope
    var progress by remember{ mutableStateOf(0.1f)}
    var loadingState by remember { mutableStateOf(false) }
    val animatedProgress = animateFloatAsState(targetValue = progress,
        animationSpec = ProgressIndicatorDefaults.ProgressAnimationSpec, label = ""
    ).value
    Card( modifier = Modifier
        .fillMaxWidth()
        .clickable {
            loadingState = true
            scope.launch {
                loadProgress { progres->
                    progress = progres
                }
                loadingState = false
                onClick()
            }
        }
        .heightIn(max = 120.dp)
        .padding(horizontal = 8.dp),
       elevation = CardDefaults.cardElevation(10.dp),
        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    ) {
            Row(modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier
                    .weight(0.6f)
                    .padding(horizontal = 20.dp)) {
                    StarRatingBar(
                        maxStars = 5,
                        rating = rating,
                        onRatingChanged = {
                        }
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        text = text,
                        color = colorResource(id = R.color.text_title),
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Normal),
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    if(loadingState){
                            LinearProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(3.dp),
                                color = MaterialTheme.colorScheme.primary
                            )

                    }
                }
                Image(modifier = Modifier
                    .weight(0.3f)
                    .fillMaxHeight()
                    .size(200.dp)
                    .clip(RoundedCornerShape(10.dp))
                    ,
                    painter = imageVector, contentDescription = null,
                    contentScale = ContentScale.Crop,

                    )
            }
            Spacer(modifier = Modifier.height(5.dp))



    }
}
/** Iterate the progress value */
suspend fun loadProgress(updateProgress: (Float) -> Unit) {
    for (i in 1..100) {
        updateProgress(i.toFloat() / 100)
        delay(100)
    }
}
@Composable
fun StarRatingBar(
    maxStars: Int = 5,
    rating: Double,
    onRatingChanged: (Float) -> Unit
) {
    val density = LocalDensity.current.density
    val starSize = (12f * density).dp
    val starSpacing = (0.5f * density).dp

    Row(
        modifier = Modifier.selectableGroup(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxStars) {
            val isSelected = i <= rating
            val icon = if (isSelected) Icons.Filled.Star else Icons.Default.Star
            val iconTintColor = if (isSelected) Color(0xFFFFC700) else Color(0x20FFFFFF)
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier
                    .selectable(
                        selected = isSelected,
                        onClick = {
                            onRatingChanged(i.toFloat())
                        }
                    )
                    .width(starSize)
                    .height(starSize)
            )

            if (i < maxStars) {
                Spacer(modifier = Modifier.width(starSpacing))
            }
        }
    }
}

@Composable
fun ProgressBar(modifier: Modifier = Modifier) {
    CircularProgressIndicator()

}
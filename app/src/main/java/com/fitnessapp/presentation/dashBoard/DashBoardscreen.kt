package com.fitnessapp.presentation.dashBoard

import android.graphics.Color
import android.graphics.Paint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitnessapp.R


@Composable
fun DashboardScreen(viewModel: WorkoutsProgressViewModel) {
    val workoutsProgress by viewModel.workoutsProgress.collectAsState()
    Column(
        modifier = Modifier
            .padding(horizontal = 30.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center, // Align at the bottom
        horizontalAlignment = CenterHorizontally
    ) {
        if (workoutsProgress.isNotEmpty()) {
            val durations = workoutsProgress.map { it.duration.toFloat() } // Y-axis data
            val days = workoutsProgress.map { it.date } // X-axis labels

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                BarGraph(
                    graphBarData = durations,
                    xAxisScaleData = days,
                    barWidth = 20.dp,
                    barColor = colorResource(id = R.color.purple_200),
                )
            }
        } else {
            Text(text = "No data available", color = colorResource(id = R.color.text_title))
        }
    }
}

@Composable
fun BarGraph(
    graphBarData: List<Float>,
    xAxisScaleData: List<String>,
    barColor: androidx.compose.ui.graphics.Color,
    barWidth: Dp
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val yAxisTextWidth = 50.dp
    val maxDuration = (graphBarData.maxOrNull() ?: 1f).coerceAtLeast(120f) // Ensuring scale of 60

    val yAxisSteps = 3 // Number of steps in Y-axis
    val yAxisScaleSpacing = 60f // Step size of 60

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp), // Increased height for better spacing
        contentAlignment = Alignment.BottomStart
    ) {
        Column(
            modifier = Modifier
                .padding(top = 40.dp, end = 3.dp)
                .height(300.dp)
                .fillMaxWidth(),
            horizontalAlignment = CenterHorizontally
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val yCoordinates = mutableListOf<Float>()
                val pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)

                (0..yAxisSteps ).forEach { i ->
                    val yValue = i * yAxisScaleSpacing
                    val yPosition = size.height - (i * size.height / yAxisSteps)

                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            yValue.toInt().toString(),
                            30f,
                            yPosition,
                            Paint().apply {
                                color = Color.BLUE.hashCode()
                                textAlign = Paint.Align.CENTER
                                textSize = 18.sp.toPx()
                            }
                        )
                    }
                    yCoordinates.add(yPosition)
                }
                // Draw horizontal grid lines
                (1..yAxisSteps).forEach {
                    drawLine(
                        start = Offset(x = yAxisTextWidth.toPx(), y = yCoordinates[it]),
                        end = Offset(x = size.width, y = yCoordinates[it]),
                        color = androidx.compose.ui.graphics.Color.Gray,
                        strokeWidth = 2f,
                        pathEffect = pathEffect
                    )
                    drawLine(
                        start = Offset(x = yAxisTextWidth.toPx(), y = size.height),
                        end = Offset(x = size.width add , y = size.height),
                        color = androidx.compose.ui.graphics.Color.Black,
                        strokeWidth = 3f
                    )
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(start = 50.dp)
                .width(screenWidth - yAxisTextWidth)
                .height(340.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Row(
                modifier = Modifier
                    .width(screenWidth)
                    .horizontalScroll(rememberScrollState()), // Enable scrolling
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                graphBarData.forEachIndexed { index, value ->
                    val graphBarHeight by animateFloatAsState(
                        targetValue = value / maxDuration,
                        animationSpec = tween(durationMillis = 1000)
                    )

                    Column(
                        modifier = Modifier.fillMaxHeight(),
                        verticalArrangement = Arrangement.Bottom,
                        horizontalAlignment = CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(topStart = 5.dp, topEnd = 5.dp))
                                .width(barWidth)
                                .height(300.dp * graphBarHeight)
                                .background(barColor)
                        )

                        Text(
                            text = xAxisScaleData[index],
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Start,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

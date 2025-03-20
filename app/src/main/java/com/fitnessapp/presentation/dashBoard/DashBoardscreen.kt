package com.fitnessapp.presentation.dashBoard

import android.content.Context
import android.graphics.Paint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitnessapp.R
import com.fitnessapp.utils.Constants


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(viewModel: WorkoutsProgressViewModel) {
    val workoutsProgress by viewModel.workoutsProgress.collectAsState()
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences(Constants.U_PREF, Context.MODE_PRIVATE)
    val weight = sharedPreferences.getFloat(Constants.WEIGHT, 55f)

    val calorie  by viewModel.caloriesBurned.observeAsState(0.0)
    val totalCalBurned = calorie * (200.0 / weight.toDouble())
    LaunchedEffect(Unit) {
        viewModel.getWorkoutsBYDate()
        viewModel.refreshWorkouts() // Refresh when page opens
    }
Column(modifier = Modifier
    .fillMaxSize()
    .verticalScroll(rememberScrollState())
    .statusBarsPadding()) {
    TopAppBar(
        title = { Text(text = "Track Your Progress",
        textAlign = TextAlign.Start,
        fontSize = 25.sp,
        fontWeight = FontWeight.SemiBold,
        color = colorResource(id = R.color.text_title))})
    Column(
        modifier = Modifier
            .weight(0.85f)
            .padding(horizontal = 20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            CaloriePieChart(totalCalBurned)
            OutlinedCard(onClick = { /*TODO*/ },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                border = BorderStroke(1.dp, colorResource(id = R.color.text_title)),
                modifier = Modifier
                    .size(width = 270.dp, height = 200.dp)
                
            ) {
                HeightSliderWithBMI(weight = weight)
                
            }
        }

        Spacer(modifier = Modifier.height(15.dp))
        if (workoutsProgress.isNotEmpty()) {
            val durations = workoutsProgress.map { it.duration.toFloat() } // Y-axis data
            val days = workoutsProgress.map { it.date } // X-axis labels
            Text(text = "Daily Workouts- time progress",
                fontSize = 18.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Center, color = colorResource(id = R.color.display_small))
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                BarGraph(
                    colors = R.color.blue,
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
}
@Composable
fun HeightSliderWithBMI(weight : Float ) {
    var height by remember { mutableFloatStateOf(170f) } // Default height in cm
    val bmi = remember(height) { weight / ((height / 100) * (height / 100)) } // BMI Formula
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Select Your Height", fontSize = 18.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "${height.toInt()} cm",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(id = R.color.text_title)
        )

        Slider(
            value = height,
            onValueChange = { height = it },
            valueRange = 100f..220f, // Height range from 100cm to 220cm
            steps = 120, // Smooth adjustments
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .fillMaxWidth()
                .width(28.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(modifier = Modifier.weight(0.7f),
                text = "BMI: ${String.format("%.2f", bmi)}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (bmi < 18.5) Color.Yellow else if (bmi < 25) colorResource(
                    id = R.color.Green
                ) else colorResource(id = R.color.Red)
            )

            Text(
                text = if (bmi < 19.5) "Under Weight" else if (bmi >= 19.5 && bmi < 25) " Normal" else "Obese",
                color = if (bmi < 18.5) Color.Yellow else if (bmi < 25) colorResource(
                    id = R.color.Green
                ) else colorResource(id = R.color.Red)


                )
        }

    }
}


@Composable
fun CaloriePieChart(caloriesBurned: Double, targetCalories: Double = 300.0) {
    val percentage = (caloriesBurned / targetCalories).coerceIn(0.0, 1.0) // Limit to 100%
    val sweepAngle = (percentage * 360).toFloat()  // Convert to degrees
    val myText = "Today's Chart"
    val textMeasurer = rememberTextMeasurer()
    val textLayoutResult = textMeasurer.measure(text = AnnotatedString(myText))
    val textSize = textLayoutResult.size
    Column(verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier = Modifier.size(150.dp)) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension / 2

            // Draw full background circle (target)
            drawArc(
                color = androidx.compose.ui.graphics.Color.LightGray,
                startAngle = 0f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = 40f, cap = StrokeCap.Round)
            )

            // Draw burned calories progress
            drawArc(
                color = Color.Blue,
                startAngle = -90f,  // Start from top
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = 50f, cap = StrokeCap.Square)
            )
            drawText(
                textMeasurer, myText,
                topLeft = Offset(
                    (this.size.width - textSize.width) / 2f,
                    (this.size.height - textSize.height) / 2f
                ),
            )
        }
        Text(
            text = "${caloriesBurned.toInt()} / ${targetCalories.toInt()} kcal",
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp)
        )

    }
}

@Composable
fun BarGraph(
    colors: Int,
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
                                color = colors
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
                        end = Offset(x = size.width , y = size.height),
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
                        animationSpec = tween(durationMillis = 1000), label = ""
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

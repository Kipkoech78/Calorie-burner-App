package com.fitnessapp.presentation.home

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fitnessapp.R
import com.fitnessapp.presentation.HomeCard
import com.fitnessapp.presentation.navgraph.Route
import com.fitnessapp.utils.Constants
import com.google.gson.Gson
import java.io.File
import java.io.IOException

@Composable
fun HomeScreen(cards: List<CardItems> , navController: NavController, ) {
    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
        Text(text = "Which plan do you want to start with",
            modifier = Modifier.padding(top = 20.dp, end= 20.dp, start = 20.dp, bottom = 20.dp),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
            color = colorResource(
            id = R.color.body))
        Column(modifier = Modifier
            .verticalScroll(rememberScrollState())
            ) {
            cards.forEach { card->
                Spacer(modifier = Modifier.height(10.dp))
                    HomeCard(text = card.desc,
                        rating = card.ratings,
                        onClick = {
                            navController.navigate("WorkoutListScreen/${card.category}")
                        },

                        imageVector = painterResource(id = card.image))
            }
        }
    }
}

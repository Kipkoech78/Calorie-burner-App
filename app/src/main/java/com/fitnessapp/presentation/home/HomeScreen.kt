package com.fitnessapp.presentation.home

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fitnessapp.R
import com.fitnessapp.presentation.HomeCard
import com.fitnessapp.presentation.navgraph.Route

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(cards: List<CardItems> , navController: NavController, ) {
    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
        TopAppBar(title = {
            Row(modifier = Modifier.heightIn(150.dp).fillMaxWidth().padding(top = 20.dp, bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Image(painter = painterResource(id = R.drawable.fitnessapplogo), contentDescription =null,
                    modifier = Modifier.clip(RoundedCornerShape(10.dp)).weight(0.2f).size(120.dp),
                    contentScale = ContentScale.Fit
                    )
                Text(text = "Which plan Do you want to Start with.",
                    modifier = Modifier.weight(0.78f),
                    textAlign = TextAlign.Start,
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = colorResource(
                        id = R.color.body))
            }
        })
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
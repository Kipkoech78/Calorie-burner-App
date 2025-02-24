package com.fitnessapp.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fitnessapp.R
import com.fitnessapp.presentation.navgraph.Route
@Composable
fun DieteticsScreen( ) {
    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
        Text(modifier = Modifier.padding(5.dp).fillMaxWidth().heightIn(70.dp),
            text = "Dietetics Implementation".repeat(5),
            fontSize = 40.sp,
            maxLines = 10,
            lineHeight = 50.sp,
            color = colorResource(id = R.color.text_title),
            style = MaterialTheme.typography.titleMedium,
            )
    }
}
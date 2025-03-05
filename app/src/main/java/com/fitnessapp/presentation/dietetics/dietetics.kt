package com.fitnessapp.presentation.dietetics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.paging.compose.LazyPagingItems
import com.fitnessapp.R
import com.fitnessapp.models.Meal

@Composable
fun DieteticsScreen(meals: LazyPagingItems<Meal>,
                   // navigateToSearch:() -> Unit,
                   // navigateToDetails: (Meal) -> Unit,
                    ) {
    Column(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()) {
        Spacer(modifier = Modifier.height(10.dp))
        ArticlesList(modifier = Modifier.padding(horizontal = 6.dp),
            meals = meals,
           // onClick = {navigateToDetails(it)}
        )
    }
}
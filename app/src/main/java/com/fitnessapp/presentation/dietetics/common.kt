package com.fitnessapp.presentation.dietetics

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fitnessapp.R
import com.fitnessapp.models.Meal
import com.fitnessapp.presentation.EmptyScreen


fun Modifier.shimmerEffect() = composed {
    val transition = rememberInfiniteTransition()
    val alpha = transition.animateFloat(initialValue = 0.2f, targetValue =0.9f , animationSpec = infiniteRepeatable(
        animation = tween(durationMillis = 1000),
        repeatMode = RepeatMode.Reverse
    ), label = ""
    ).value
    this.background(color = colorResource(id = R.color.shimmer).copy(alpha = alpha))
}

@Composable
fun ArticlesList(modifier: Modifier = Modifier,
                 meals: LazyPagingItems<Meal>,
               //  onClick: (Meal) -> Unit
) {
    val handlePagingResult = handlePagingResults(article = meals)
    if (handlePagingResult){
        LazyColumn(modifier= Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(all = 3.dp)
        ) {
            items(count = meals.itemCount){
                meals[it]?.let {
                    ArticleCard(meal = it,
                        //onClick = {onClick(it)}
                    )
                }
            }
        }
    }

}
@Composable
fun ArticleList(
    articles: List<Meal>,
    onClick: (Meal) -> Unit) {
    LazyColumn(modifier= Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(all = 3.dp)
    ) {
        items(count = articles.size){
            val article =  articles[it]
            ArticleCard(meal = article,
                //TODO
                //onClick = {onClick(article)}
            )
        }
    }
}
@Composable
fun handlePagingResults( article: LazyPagingItems<Meal>)
        :Boolean{
    val loadState = article.loadState
    val error = when{
        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
        else -> null
    }
    return  when{
        loadState.refresh is LoadState.Loading ->{
            ShimmerEffect()
            false
        }
        error != null ->{
            EmptyScreen()
            false
        }
        else ->{
            true
        }
    }

}
@Composable
fun ShimmerEffect(){
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        repeat(10){
            MealShimmerEffect(modifier = Modifier.padding(horizontal = 6.dp))
        }

    }
}

@Composable
fun ArticleCard(modifier: Modifier = Modifier,
                meal: Meal,
               // onClick:()-> Unit
) {
    val context = LocalContext.current
    Column(modifier = Modifier.clickable {
        //TODO
//        onClick()
    }
    ) {
        AsyncImage(
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth().height(350.dp)
                .clip(RoundedCornerShape(20.dp)),
            model = ImageRequest.Builder(context).data(meal.strMealThumb).build(),
            contentDescription =null )
        Column(
            modifier = Modifier
                .padding(3.dp),

            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = meal.strMeal, style = MaterialTheme.typography.bodyMedium,
                color = colorResource(id = R.color.text_title),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis)
            Column(horizontalAlignment = Alignment.Start) {
                Text(text = meal.strCategory.toString(), style = MaterialTheme.typography.labelMedium,
                    color = colorResource(id = R.color.body)
                )
//                Spacer(modifier = Modifier.width(6.dp))
//                Icon(painter = painterResource(id = R.drawable.ic_time), contentDescription =null,
//                    modifier = Modifier.size(12.dp),
//                    tint = colorResource(id = R.color.body)
//                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = meal.strInstructions.toString(), style = MaterialTheme.typography.labelMedium,
                    color = colorResource(id = R.color.body)
                )
            }
        }
    }
}

@Composable
fun MealShimmerEffect(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Column(modifier = Modifier) {
        Box(
            modifier = Modifier
                .height(300.dp).fillMaxWidth()
                .clip(MaterialTheme.shapes.medium)
                .shimmerEffect()
        )

        Column(
            modifier = Modifier
                .padding(3.dp)
                .height(96.dp),
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth().height(100.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .shimmerEffect()
            )
            Column(horizontalAlignment = Alignment.Start) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth().height(50.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .shimmerEffect()
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth().height(150.dp)
                        .clip(MaterialTheme.shapes.medium)
                        .shimmerEffect()
                )
                Spacer(modifier = Modifier.width(6.dp))

            }

        }
    }

}



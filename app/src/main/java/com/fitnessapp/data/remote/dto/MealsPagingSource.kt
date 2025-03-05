package com.fitnessapp.data.remote.dto

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fitnessapp.models.Meal

class MealsPagingSource(private val foodMealsAPI: foodmealsAPI, ) :
    PagingSource<Int, Meal>() {
        private var totalMealCount = 0
    override fun getRefreshKey(state: PagingState<Int, Meal>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1)?:anchorPage?.nextKey?.minus(1)
        }

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Meal> {
        val page = params.key?:1
        return try{
            val mealResponse = foodMealsAPI.listMealsByFirstLetter(page = page, firstLetter = "a")
            totalMealCount += mealResponse.meals.size
            val mealsReceived = mealResponse.meals.distinctBy { it.idMeal }
            //return
            LoadResult.Page(
                data = mealsReceived,
                nextKey = if(totalMealCount == mealResponse.totalResults) null else page + 1,
                prevKey = null
            )

        }catch (e:Exception){
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}
package com.fitnessapp.data.remote.dto

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.fitnessapp.models.Meal

class SearchMealsPagingSource(
    private  val api: foodmealsAPI,
    private val searchQuery: String,
    ): PagingSource<Int, Meal>() {
    private var totalMealsCount = 0
    override fun getRefreshKey(state: PagingState<Int, Meal>): Int? {
        return state.anchorPosition?.let{ anchorpos ->
            val anchorPage = state.closestPageToPosition(anchorpos)
            anchorPage?.prevKey?.plus(1)?:anchorPage?.nextKey?.minus(1)

        }

    }
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Meal> {
        val page = params.key?:1
        return  try{
            val newsResponse = api.searchMealByName(searchQuery = searchQuery, page = page)
            totalMealsCount +=  newsResponse.meals.size
            val articles = newsResponse.meals.distinctBy { it.idMeal }
            LoadResult.Page(
                data = articles,
                nextKey = if(totalMealsCount == newsResponse.totalResults) null else page + 1,
                prevKey = null
            )

        }catch (e:Exception){
            e.printStackTrace()
            LoadResult.Error(throwable = e)
        }
    }
}
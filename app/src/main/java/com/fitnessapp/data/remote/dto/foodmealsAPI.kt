package com.fitnessapp.data.remote.dto

import com.fitnessapp.models.Food
import com.fitnessapp.models.FoodResponse
import com.fitnessapp.models.MealResponse
import com.fitnessapp.models.genModels.Data
import com.fitnessapp.models.genModels.mealmodels
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface foodmealsAPI {
    @GET("search.php")
    suspend fun searchMealByName(
        @Query("s") searchQuery: String,
        @Query("page") page: Int,

    ): MealResponse

    @GET("search.php")
    suspend fun listMealsByFirstLetter(
        @Query("page") page: Int,
        @Query("f") firstLetter: String
    ): MealResponse
}
interface FoodAPi{
    @GET("get-aggregate/random")

    suspend fun getRandomMeals(): mealmodels
}
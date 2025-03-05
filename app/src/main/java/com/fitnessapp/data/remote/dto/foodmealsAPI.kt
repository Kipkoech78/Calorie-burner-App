package com.fitnessapp.data.remote.dto

import com.fitnessapp.models.MealResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

val searchQuery = "s"
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
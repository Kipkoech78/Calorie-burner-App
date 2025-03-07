package com.fitnessapp.data.repository.manager

import android.content.Context
import android.util.Log
import com.fitnessapp.models.DayMeal
import com.fitnessapp.models.WorkoutVideo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WorkoutRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun loadWorkoutVideos(): List<WorkoutVideo> {
        return try {
            val json = context.assets.open("workouts.json").bufferedReader().use { it.readText() }
            val type = object : TypeToken<List<WorkoutVideo>>() {}.type
            Gson().fromJson(json, type)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
    fun loadMealPlan(): List<DayMeal> {
        return try {
            val jsonObj = context.assets.open("meals.json").bufferedReader().use { it.readText() }
            //  Log.d("data", jsonObj)
            val type = object :TypeToken<List<DayMeal>>() {}.type
             Gson().fromJson(jsonObj, type)
        }catch (e : Exception) {
            e.printStackTrace()
            Log.e("error ", "Error ${e.message}")
            emptyList()
        }

    }

}

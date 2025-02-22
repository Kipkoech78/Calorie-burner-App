package com.fitnessapp.data.repository.manager

import android.content.Context
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
}

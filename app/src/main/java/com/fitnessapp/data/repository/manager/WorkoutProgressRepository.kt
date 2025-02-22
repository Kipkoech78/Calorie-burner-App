package com.fitnessapp.data.repository.manager

import com.fitnessapp.data.repository.local.WorkoutsProgressDao

import com.fitnessapp.models.WorkoutsProgress
import javax.inject.Inject
import kotlin.time.Duration

class WorkoutProgressRepository(
    private val workoutsProgressDao: WorkoutsProgressDao
) {
    suspend fun saveWorkouts(progress: WorkoutsProgress){
        workoutsProgressDao.insertWorkouts(
            WorkoutsProgress(
                id = progress.id,
                date = progress.date,
                //workoutName = progress.workoutName,
                duration = progress.duration,
            )
        )
    }
    suspend fun getWorkoutsByDate(date: String) :List<WorkoutsProgress> {
        return workoutsProgressDao.getWorkoutsByDate(date).map {
            WorkoutsProgress( it.id, it.date,  it.duration)
        }
    }
    suspend fun getAllWorkouts(): List<WorkoutsProgress> {
        return workoutsProgressDao.getAllWorkouts().map {
            WorkoutsProgress(  it.id, it.date, it.duration)
        }
    }
    suspend fun  updateWorkoutDuration(date: String, duration: Int){
        workoutsProgressDao.updateWorkoutDuration(
            date, duration
        )
    }
}
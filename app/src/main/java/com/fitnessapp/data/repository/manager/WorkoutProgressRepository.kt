package com.fitnessapp.data.repository.manager

import com.fitnessapp.data.repository.local.WorkoutsProgressDao
import com.fitnessapp.models.WorkoutVideo

import com.fitnessapp.models.WorkoutsProgress
import kotlinx.coroutines.flow.Flow
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
     fun getFavWorkouts(): Flow<List<WorkoutVideo>> {
        return workoutsProgressDao.getFavWorkouts()
    }
    suspend fun  updateWorkoutDuration(date: String, duration: Int){
        workoutsProgressDao.updateWorkoutDuration(
            date, duration
        )
    }
    suspend fun delete(video:WorkoutVideo){
        workoutsProgressDao.delete(video)
    }
    suspend fun addFavWorkouts(video: WorkoutVideo){
      workoutsProgressDao.addFavWorkouts(
            WorkoutVideo(videoResId =video.videoResId,
                name= video.name,
                category =  video.category,
               gender =  video.gender,
                description = video.description)
        )
    }
}
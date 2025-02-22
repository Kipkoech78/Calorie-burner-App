package com.fitnessapp.domain.useCases.progressUseCases

import com.fitnessapp.data.repository.manager.WorkoutProgressRepository
import com.fitnessapp.data.repository.manager.WorkoutRepository
import com.fitnessapp.models.WorkoutsProgress
import kotlinx.coroutines.flow.Flow

class GetWorkoutProgress(private val repository: WorkoutProgressRepository) {
    suspend operator fun invoke() : List<WorkoutsProgress>{
        return repository.getAllWorkouts()
    }
}
class GetWorkoutProgressByDate(private val repository: WorkoutProgressRepository){
    suspend operator fun invoke(date: String): List<WorkoutsProgress> {
        return repository.getWorkoutsByDate(date)
    }
}

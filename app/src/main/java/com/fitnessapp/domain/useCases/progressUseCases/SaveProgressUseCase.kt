package com.fitnessapp.domain.useCases.progressUseCases

import com.fitnessapp.data.repository.manager.WorkoutProgressRepository
import com.fitnessapp.models.WorkoutsProgress

class SaveProgressUseCase(private val repository: WorkoutProgressRepository) {
    suspend operator fun invoke(progress: WorkoutsProgress){
        repository.saveWorkouts(progress)
    }
}
class UpdateWorkoutsProgress(private val repository: WorkoutProgressRepository){
    suspend operator fun invoke(date: String, duration: Int){
        repository.updateWorkoutDuration(date, duration)
    }
}
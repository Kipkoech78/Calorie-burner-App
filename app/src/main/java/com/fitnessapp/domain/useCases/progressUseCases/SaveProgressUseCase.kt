package com.fitnessapp.domain.useCases.progressUseCases

import com.fitnessapp.data.repository.manager.WorkoutProgressRepository
import com.fitnessapp.models.WorkoutVideo
import com.fitnessapp.models.WorkoutsProgress
import kotlinx.coroutines.flow.Flow

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
class  Delete( private val repository: WorkoutProgressRepository){
    suspend operator fun invoke(video: WorkoutVideo,){
        repository.delete(video)
    }
}
class  GetVafWorkouts(private val repository: WorkoutProgressRepository){
     operator fun invoke(): Flow<List<WorkoutVideo>>{
      return  repository.getFavWorkouts()
    }
}

class AddFavWorkouts(private val repository: WorkoutProgressRepository){
    suspend operator fun invoke(video: WorkoutVideo){
        repository.addFavWorkouts(video)

    }
}
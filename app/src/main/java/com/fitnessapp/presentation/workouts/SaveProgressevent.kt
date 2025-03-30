package com.fitnessapp.presentation.workouts
import com.fitnessapp.models.WorkoutVideo
import com.fitnessapp.models.WorkoutsProgress

sealed class SaveProgressEvent {
    data class getWorkoutsProgressByDate(val date: String) : SaveProgressEvent()
    data class UpdateProgress(val date: String ,val duration: Int): SaveProgressEvent()
    data class UpsertProgress(val progress: WorkoutsProgress): SaveProgressEvent()
    data class InsertFavWorkouts(
        val video: WorkoutVideo
        ):SaveProgressEvent()
    object RemoveSideEffect:  SaveProgressEvent()
}
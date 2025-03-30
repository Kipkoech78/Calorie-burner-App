package com.fitnessapp.domain.useCases.progressUseCases

data class ProgressUseCases(
    val getWorkoutProgress: GetWorkoutProgress,
    val saveProgressUseCase: SaveProgressUseCase,
    val getWorkoutProgressByDate: GetWorkoutProgressByDate,
    val updateWorkoutsProgress: UpdateWorkoutsProgress,
    val delete: Delete,
    val getVafWorkouts: GetVafWorkouts,
    val addFavWorkouts: AddFavWorkouts
)

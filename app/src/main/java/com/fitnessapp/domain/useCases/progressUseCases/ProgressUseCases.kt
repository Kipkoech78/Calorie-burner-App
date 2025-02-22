package com.fitnessapp.domain.useCases.progressUseCases

data class ProgressUseCases(
    val getWorkoutProgress: GetWorkoutProgress,
    val saveProgressUseCase: SaveProgressUseCase,
    val getWorkoutProgressByDate: GetWorkoutProgressByDate,
    val updateWorkoutsProgress: UpdateWorkoutsProgress
)

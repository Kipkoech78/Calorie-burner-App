package com.fitnessapp.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fitnessapp.models.WorkoutVideo
import com.fitnessapp.models.WorkoutsProgress

@Database(entities = [WorkoutsProgress::class, WorkoutVideo::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract val workoutsProgressDao : WorkoutsProgressDao
}
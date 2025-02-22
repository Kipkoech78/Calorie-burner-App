package com.fitnessapp.data.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fitnessapp.models.WorkoutsProgress

@Database(entities = [WorkoutsProgress::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract val workoutsProgressDao : WorkoutsProgressDao
}
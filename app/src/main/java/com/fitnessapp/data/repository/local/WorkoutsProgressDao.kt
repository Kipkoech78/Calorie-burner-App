package com.fitnessapp.data.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.fitnessapp.models.WorkoutsProgress

@Dao
interface WorkoutsProgressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkouts(progress: WorkoutsProgress)
    @Query("SELECT * FROM WorkoutsProgress WHERE date= :date")
    suspend fun getWorkoutsByDate(date: String ) : List<WorkoutsProgress>
    @Query("SELECT * FROM WorkoutsProgress ")
    suspend fun getAllWorkouts(): List<WorkoutsProgress>
    @Query("UPDATE WorkoutsProgress SET duration = duration + :additionalDuration WHERE date = :date")
    suspend fun updateWorkoutDuration(date: String, additionalDuration: Int)
}
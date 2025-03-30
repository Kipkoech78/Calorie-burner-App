package com.fitnessapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date
@Parcelize
@Entity
data class WorkoutVideo(
    @PrimaryKey val videoResId: String = "",  // Resource ID of the video
    val name : String,
    val category: String,
    val gender: String,
    val description: String
): Parcelable
@Parcelize
@Entity
data class WorkoutsProgress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: String,
  //  val workoutName: String,
    val duration: Int
): Parcelable

@Parcelize
@Entity
data class ViewedWorkoutProgress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // val date: String,
    val video: String,
    val workoutName: String,
    val description: String,
    val duration: Int
): Parcelable



package com.fitnessapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.Date
@Parcelize
data class WorkoutVideo(
    val videoResId: String,  // Resource ID of the video
    val name : String = "name of the workout",
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



package com.fitnessapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

data class MealResponse(
    val meals: List<Meal>,
    val status: String,
    val totalResults:Int
    )

@Parcelize
@Entity
data class Meal(
    @PrimaryKey val idMeal: String,
    val strMeal: String,
    val strCategory: String?,
    val strArea: String?,
    val strInstructions: String?,
    val strMealThumb: String?,
    val strYoutube: String?
): Parcelable

//data class MealPlan(val days: List<DayMeal>)
data class DayMeal(val day: String,
                   val meals: Meals
)
data class Meals(
    val breakfast: String,
    val secondBreakfast: String,
    val lunch: String,
    val afternoonSnack: String,
    val dinner: String
)

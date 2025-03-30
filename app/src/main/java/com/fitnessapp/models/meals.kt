package com.fitnessapp.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fitnessapp.models.genModels.Data
import kotlinx.parcelize.Parcelize

data class MealResponse(
    val meals: List<Meal>,
    val status: String,
    val totalResults:Int
    )
data class FoodResponse(
    val meals: List<Data>,
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
data class DayMeal(val days: Day,
                   val meals: Meals
)
data class Meals(
    val breakfast: MealString,
    val secondBreakfast: MealString,
    val lunch: MealString,
    val afternoonSnack: MealString,
    val dinner: MealString
)
data class Day(
    val day: String,
    val image: String
)
data class MealString(
    val type: String,
    val whatToEat: String
)
@Parcelize
data class Food(
    val name: String,
    val category: String,
    val calories: Int,
    val protein: Int,
    val carbs: Int,
    val fat: Int,
    val goal: List<String>
):Parcelable


package com.fitnessapp.presentation.home

import androidx.annotation.DrawableRes


data class CardItems(
    val ratings: Double,
    val gender : String,
    val desc : String,
    val category: String,

    @DrawableRes val image: Int
)
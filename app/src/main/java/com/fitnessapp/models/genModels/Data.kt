package com.fitnessapp.models.genModels

data class Data(
    val __v: Int,
    val _id: String,
    val calories: Int,
    val carbs: Int,
    val category: String,
    val fat: Int,
    val goal: List<String>,
    val name: String,
    val protein: Int
)
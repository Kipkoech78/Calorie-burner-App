package com.fitnessapp.domain.useCases

data class AppEntryUseCases(
    val readAppEntry: ReadAppEntry,
    val saveAppEntry: SaveAppEntry,
    val readGender: ReadGender,
    val readWeight: ReadWeight,
    val saveGender: SaveGender,
    val saveWeight: SaveWeight
)

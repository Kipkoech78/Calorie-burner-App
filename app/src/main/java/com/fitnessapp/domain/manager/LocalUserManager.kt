package com.fitnessapp.domain.manager

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

interface LocalUserManager {
    suspend fun saveAppEntry()
    fun readAppEntry(): Flow<Boolean>
    suspend fun saveGender(gender: String)
    fun readGender(): Flow<String?>
    suspend fun saveWeight(weight: Float)
    fun readWeight(): Flow<Float?>
}

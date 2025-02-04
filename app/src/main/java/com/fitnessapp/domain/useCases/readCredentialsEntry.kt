package com.fitnessapp.domain.useCases

import com.fitnessapp.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

class ReadWeight(private val localUserManager: LocalUserManager) {
    suspend operator fun invoke() : Flow<Float ?> {
        return localUserManager.readWeight()
    }
}
class ReadGender(private val localUserManager: LocalUserManager) {
    suspend operator fun invoke() : Flow<String ?> {
        return localUserManager.readGender()
    }
}
class SaveWeight(private val localUserManager: LocalUserManager) {
    suspend operator fun invoke(weight: Float) {
        localUserManager.saveWeight(weight)
    }
}
class SaveGender(private val localUserManager: LocalUserManager) {
    suspend operator fun invoke(gender: String)  {
        localUserManager.saveGender(gender)
    }
}
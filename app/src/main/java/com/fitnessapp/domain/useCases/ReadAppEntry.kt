package com.fitnessapp.domain.useCases

import com.fitnessapp.domain.manager.LocalUserManager
import kotlinx.coroutines.flow.Flow

class ReadAppEntry(private val localUserManager: LocalUserManager) {
    suspend operator fun invoke() : Flow<Boolean> {
        return localUserManager.readAppEntry()
    }
}
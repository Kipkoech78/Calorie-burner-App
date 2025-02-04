package com.fitnessapp.domain.useCases

import com.fitnessapp.domain.manager.LocalUserManager

class SaveAppEntry(private val localUserManager: LocalUserManager) {
    suspend operator fun invoke(){
        localUserManager.saveAppEntry()
    }
}
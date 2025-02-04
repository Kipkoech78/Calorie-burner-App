package com.fitnessapp.presentation.credentials

sealed class AddCredentialsEvent {
     object SaveAppEntry : AddCredentialsEvent()
     data class SaveGender(val gender: String) : AddCredentialsEvent()
     data class SaveWeight(val weight: Float) : AddCredentialsEvent()


}
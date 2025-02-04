package com.fitnessapp.presentation.credentials

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitnessapp.domain.useCases.AppEntryUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SaveCredentialsViewModel @Inject constructor( private val appEntryUseCases: AppEntryUseCases ): ViewModel() {
    private fun saveAppEntry() {
        viewModelScope.launch {
            appEntryUseCases.saveAppEntry()
        }

    }
    private fun saveGender(gender: String){
        viewModelScope.launch {
            appEntryUseCases.saveGender(gender)
        }
    }
    private fun saveWeight(weight: Float){
        viewModelScope.launch {
            appEntryUseCases.saveWeight(weight)
        }
    }
    fun onEvent(event: AddCredentialsEvent){
        when(event){
            is AddCredentialsEvent.SaveAppEntry ->{
                saveAppEntry()
            }
            is AddCredentialsEvent.SaveWeight ->{
                saveWeight(event.weight)
            }
            is AddCredentialsEvent.SaveGender -> {
                saveGender(event.gender)
            }
        }
    }

}
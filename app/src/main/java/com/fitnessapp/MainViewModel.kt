package com.fitnessapp

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitnessapp.domain.useCases.AppEntryUseCases
import com.fitnessapp.presentation.navgraph.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val appEntryPoint: AppEntryUseCases)
    : ViewModel(){
        var SplashCondition by mutableStateOf(true)
            private set
    var startDestination by mutableStateOf(Route.HomeScreen.route)
        private set
    init {
        appEntryPoint.readAppEntry().onEach { shouldStartHome ->
            if(shouldStartHome){
                startDestination = Route.HomeScreen.route

            }else{
                startDestination = Route.AddCredentialsScreen.route
            }
            delay(300)
            SplashCondition = false
        }.launchIn(viewModelScope)
    }
}
package com.fitnessapp.presentation.favourites

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitnessapp.domain.useCases.progressUseCases.ProgressUseCases
import com.fitnessapp.models.WorkoutVideo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavViewModel @Inject constructor(
    private val useCases: ProgressUseCases
) : ViewModel() {
    private val _state = mutableStateOf(FavouritesState())
    val state: State<FavouritesState> = _state

    init {
        getFavourites()
    }

    private fun getFavourites(){
        useCases.getVafWorkouts().onEach {
            _state.value = _state.value.copy(videos = it.reversed())
        }.launchIn(viewModelScope)
    }
    fun deleteFavWorkouts(video: WorkoutVideo){
        viewModelScope.launch {
            useCases.delete(video)
            getFavourites()
        }
    }
}

data class FavouritesState(
    val videos : List<WorkoutVideo> = emptyList()
)

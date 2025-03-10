package com.fitnessapp.presentation.workouts

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitnessapp.data.repository.manager.WorkoutRepository
import com.fitnessapp.domain.useCases.progressUseCases.ProgressUseCases
import com.fitnessapp.models.WorkoutVideo
import com.fitnessapp.models.WorkoutsProgress
import com.fitnessapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    //add Use case
    private val useCase: ProgressUseCases,
    //

    private val repository: WorkoutRepository
) : ViewModel() {


    var sideEffect by mutableStateOf<String?>(null)
        private set
    //useCAse Usage

    fun onEvent(event: SaveProgressEvent){
        when(event){
            is SaveProgressEvent.UpsertProgress ->{
                viewModelScope.launch {

                    upsertProgress(event.progress)
                    sideEffect = "Workouts Saved successfully"
                    Log.d("event saver", "${event.progress}")
                }
            }
            is SaveProgressEvent.UpdateProgress ->{
                viewModelScope.launch {
                    UpdateWorkoutProgress(event.date, event.duration)
                    sideEffect = "Workouts Updated successfully"
                }
            }
            is SaveProgressEvent.getWorkoutsProgressByDate ->{
                viewModelScope.launch {
                    getWorkoutsByDate(event.date)
                    sideEffect = " Workout fetched successfuly"
                }
            }
            is SaveProgressEvent.RemoveSideEffect ->{
                sideEffect = null
            }
        }
    }
    private suspend fun upsertProgress(progress: WorkoutsProgress){
        useCase.saveProgressUseCase(progress = progress)
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val progressSaved = useCase.getWorkoutProgress()
        Log.d("Progress Jobs", "$progressSaved")
        val existingProgress = useCase.getWorkoutProgressByDate(date = date)
        Log.d("existing Progress", "$existingProgress")
//        if(existingProgress.isNotEmpty()){
//            useCase.updateWorkoutsProgress(progress.date, duration = progress.duration )
//        }else{
//            useCase.saveProgressUseCase(progress = progress)
//        }

    }
    private suspend fun UpdateWorkoutProgress(date: String, duration: Int){
        useCase.updateWorkoutsProgress(date, duration)
    }
    private suspend fun getWorkoutsByDate(date: String){
        useCase.getWorkoutProgressByDate(date)
    }
    //
    private val _workouts = MutableStateFlow<List<WorkoutVideo>>(emptyList())
    val workouts: StateFlow<List<WorkoutVideo>> = _workouts
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory
    private val _filteredWorkouts = MutableStateFlow<List<WorkoutVideo>>(emptyList())
    val filteredWorkouts: StateFlow<List<WorkoutVideo>> = _filteredWorkouts
    init {
        loadWorkouts()
    }
    private fun loadWorkouts() {
        viewModelScope.launch {
            _workouts.value = repository.loadWorkoutVideos()
        }
    }
    fun filterWorkoutsByCategory(category: String, gender: String) {
        _selectedCategory.value = category
        _filteredWorkouts.value = _workouts.value.filter { it.category == category  && it.gender == gender }
    }
}

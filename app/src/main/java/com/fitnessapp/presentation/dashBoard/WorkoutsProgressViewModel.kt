package com.fitnessapp.presentation.dashBoard

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitnessapp.data.repository.manager.WorkoutProgressRepository
import com.fitnessapp.domain.useCases.progressUseCases.ProgressUseCases
import com.fitnessapp.models.WorkoutsProgress
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class WorkoutsProgressViewModel @Inject constructor(
    private val useCases: ProgressUseCases
):ViewModel() {
    var sideEffect by mutableStateOf<String?>(null)
        private set
    private val _workoutsProgress = MutableStateFlow<List<WorkoutsProgress>>(emptyList())
    val workoutsProgress: StateFlow<List<WorkoutsProgress>> = _workoutsProgress

init {
    getWorkouts()
}
    private fun getWorkouts() {
        viewModelScope.launch {
            try {
                val progressList = useCases.getWorkoutProgress()
                // Group workouts by date and sum durations

                val grouped = progressList.groupBy { it.date }
                Log.d("grouped data", "$grouped")

                val aggregatedWorkouts = progressList
                    .groupBy { it.date }
                    .map { (date, workouts) ->
                        WorkoutsProgress(id =(0..1000).random() ,  date, workouts.sumOf { it.duration })
                    }
                    .sortedBy { it.date } // Ensure the dates are in order
                _workoutsProgress.value = aggregatedWorkouts
                Log.d("raw progress data", "$progressList")

                Log.d("progress data", "$aggregatedWorkouts")
            } catch (e: Exception) {
                sideEffect = "Error fetching data: ${e.message}"
            }
        }
    }
    // Function to trigger refresh
    fun refreshWorkouts() {
        getWorkouts()
    }
}
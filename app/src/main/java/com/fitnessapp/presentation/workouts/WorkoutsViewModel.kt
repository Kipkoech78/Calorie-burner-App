package com.fitnessapp.presentation.workouts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fitnessapp.data.repository.local.WorkoutRepository
import com.fitnessapp.models.WorkoutVideo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel

class WorkoutViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {

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

    fun filterWorkoutsByCategory(category: String) {
        _selectedCategory.value = category
        _filteredWorkouts.value = _workouts.value.filter { it.category == category }
    }
}

//
//@HiltViewModel
//class com.fitnessapp.presentation.workouts.WorkoutViewModel @Inject constructor(
//    @ApplicationContext private val context: Context
//) : ViewModel() {
//
//    private val _workouts = MutableStateFlow<List<WorkoutVideo>>(emptyList())
//    val workouts: StateFlow<List<WorkoutVideo>> = _workouts
//
//    private val _selectedCategory = MutableStateFlow<String?>(null)
//    val selectedCategory: StateFlow<String?> = _selectedCategory
//
//    private val _filteredWorkouts = MutableStateFlow<List<WorkoutVideo>>(emptyList())
//    val filteredWorkouts: StateFlow<List<WorkoutVideo>> = _filteredWorkouts
//
//    init {
//        loadWorkouts()
//    }
//
////    private fun loadWorkouts() {
////        viewModelScope.launch {
////            // Load workouts from JSON in assets
////            val json = context.assets.open("workouts.json").bufferedReader().use { it.readText() }
////            val type = object : TypeToken<List<WorkoutVideo>>() {}.type
////            val workouts = Gson().fromJson<List<WorkoutVideo>>(json, type)
////            _workouts.value = workouts
////        }
////    }
//    private fun loadWorkouts(): List<WorkoutVideo> {
//        return try {
//            val json = context.assets.open("workouts.json").bufferedReader().use { it.readText() }
//            val type = object : TypeToken<List<WorkoutVideo>>() {}.type
//            Gson().fromJson(json, type)
//        } catch (e: Exception) {
//            e.printStackTrace()
//            emptyList()
//        }
//    }
//
//    fun filterWorkoutsByCategory(category: String) {
//        _selectedCategory.value = category
//        _filteredWorkouts.value = _workouts.value.filter { it.category == category }
//    }
//}
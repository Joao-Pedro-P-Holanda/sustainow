package io.github.sustainow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.domain.model.Routine
import io.github.sustainow.domain.model.RoutineTask
import io.github.sustainow.domain.model.RoutineTaskMetaData
import io.github.sustainow.repository.routine.RoutineRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _currentRoutine = MutableStateFlow<Routine?>(null)
    val currentRoutine: StateFlow<Routine?> = _currentRoutine.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _tasks = MutableStateFlow<List<RoutineTask>>(emptyList())
    val tasks: StateFlow<List<RoutineTask>> = _tasks.asStateFlow()

    fun getUserRoutine(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val routine = routineRepository.getRoutine(userId)
                _currentRoutine.value = routine
                routine?.id?.let { getRoutineTasks(it) }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getRoutineTasks(routineId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val taskList = routineRepository.getTasks(routineId)
                _tasks.value = taskList
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTask(taskMetaData: RoutineTaskMetaData) {
        val routineId = _currentRoutine.value?.id ?: return
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val updatedRoutine = routineRepository.addTaskToRoutine(routineId, taskMetaData)
                _currentRoutine.value = updatedRoutine
                _tasks.value = updatedRoutine.taskList
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateTaskCompletion(taskId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val updatedTask = routineRepository.updateTaskAsComplete(taskId)
                _tasks.value = _tasks.value.map { if (it.id == taskId) updatedTask else it }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val success = routineRepository.deleteTask(taskId)
                if (success) {
                    _tasks.value = _tasks.value.filterNot { it.id == taskId }
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

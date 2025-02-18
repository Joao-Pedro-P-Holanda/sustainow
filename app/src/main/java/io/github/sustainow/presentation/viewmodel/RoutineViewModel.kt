package io.github.sustainow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.domain.model.Routine
import io.github.sustainow.domain.model.RoutineTask
import io.github.sustainow.domain.model.RoutineTaskMetaData
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val authService: AuthService // Inject AuthService
) : ViewModel() {

    private val _currentRoutine = MutableStateFlow<Routine?>(null)
    val currentRoutine: StateFlow<Routine?> = _currentRoutine

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isError = MutableStateFlow<String?>(null)
    val isError: StateFlow<String?> = _isError

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    init {
        // Initialize the routine with the logged-in user's ID
        initializeRoutineForUser()
    }

    private fun initializeRoutineForUser() {
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = null
            try {
                // Check if the user is logged in
                when (val userState = authService.user.value) {
                    is UserState.Logged -> {
                        // Create a new routine with the user's ID
                        _currentRoutine.value = Routine(
                            userId = userState.user.uid.toString(), // Use the logged-in user's ID
                            tasks = mutableListOf()
                        )
                    }
                    else -> {
                        _isError.value = "User not logged in"
                    }
                }
            } catch (e: Exception) {
                _isError.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun addTaskToRoutine(task: RoutineTaskMetaData) {
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = null
            try {
                // Ensure the user is logged in
                when (val userState = authService.user.value) {
                    is UserState.Logged -> {
                        if (_currentRoutine.value == null) {
                            // Create a new routine with the user's ID
                            _currentRoutine.value = Routine(
                                userId = userState.user.uid, // Use the logged-in user's ID
                                taskList = mutableListOf(task)
                            )
                        } else {
                            // Add the task to the existing routine
                            _currentRoutine.value?.taskList?.add(task)
                        }
                        _isSuccess.value = true
                    }
                    else -> {
                        _isError.value = "User not logged in"
                    }
                }
            } catch (e: Exception) {
                _isError.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun markTaskAsComplete(task: RoutineTask) {
        viewModelScope.launch {
            _isLoading.value = true
            _isError.value = null
            try {
                // Ensure the user is logged in
                when (authService.user.value) {
                    is UserState.Logged -> {
                        _currentRoutine.value?.taskList?.find { it.id == task.id }?.let {
                            it.complete = true
                        }
                        _isSuccess.value = true
                    }
                    else -> {
                        _isError.value = "User not logged in"
                    }
                }
            } catch (e: Exception) {
                _isError.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}
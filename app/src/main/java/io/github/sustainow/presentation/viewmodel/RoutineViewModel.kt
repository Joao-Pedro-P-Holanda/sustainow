package io.github.sustainow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.sustainow.domain.model.Routine
import io.github.sustainow.domain.model.Task
import io.github.sustainow.presentation.ui.utils.toLocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import java.util.UUID

class RoutineViewModel : ViewModel() {

    // Estados para gerenciar o carregamento, erro e sucesso
    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success.asStateFlow()

    // Lista de tarefas sugeridas
    private val _suggestedTasks = MutableStateFlow<List<Task>>(emptyList())
    val suggestedTasks: StateFlow<List<Task>> = _suggestedTasks.asStateFlow()

    // Rotina atual do usuário
    private val _routine = MutableStateFlow<Routine?>(null)
    val routine: StateFlow<Routine?> = _routine.asStateFlow()



    fun fetchSuggestedTasks() {
        _loading.value = true
        viewModelScope.launch {
            try {
                // Simulação de uma chamada assíncrona para buscar tarefas sugeridas
                val tasks = listOf(
                    Task(1, "Tarefa 1", "Descrição 1", 1, 1, "Área 1", false, Clock.System.now().toLocalDate()),
                    Task(2, "Tarefa 2", "Descrição 2", 1, 2, "Área 2", false, Clock.System.now().toLocalDate())
                )
                _suggestedTasks.value = tasks
                _success.value = true
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun addTaskToRoutine(task: Task) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val currentRoutine = _routine.value
                val updatedTasks = currentRoutine?.listOfTasks?.toMutableList() ?: mutableListOf()
                updatedTasks.add(task)

                val newRoutine = currentRoutine?.copy(listOfTasks = updatedTasks) ?: Routine(
                    id = 1, // ID da rotina, pode ser gerado ou obtido de outra forma
                    userId = UUID.randomUUID(), // ID do usuário, pode ser obtido de outra forma
                    listOfTasks = updatedTasks
                )

                _routine.value = newRoutine
                _success.value = true
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    // Método para atualizar uma tarefa como completa
    fun completeTask(taskId: Int) {
        _loading.value = true
        viewModelScope.launch {
            try {
                val currentRoutine = _routine.value
                val updatedTasks = currentRoutine?.listOfTasks?.map { task ->
                    if (task.id == taskId) {
                        task.copy(complete = true)
                    } else {
                        task
                    }
                } ?: emptyList()

                val newRoutine = currentRoutine?.copy(listOfTasks = updatedTasks)
                _routine.value = newRoutine
                _success.value = true
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }
}
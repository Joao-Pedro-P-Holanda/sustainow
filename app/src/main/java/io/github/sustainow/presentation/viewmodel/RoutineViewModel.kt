package io.github.sustainow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.sustainow.domain.model.Routine
import io.github.sustainow.domain.model.RoutineTask
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.toLocalDateTime

class RoutineViewModel : ViewModel() {

    // Estado da rotina atual do usuário
    private val _currentRoutine = MutableStateFlow<Routine?>(null)
    val currentRoutine: StateFlow<Routine?> = _currentRoutine.asStateFlow()

    // Estados de loading, error e sucesso
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    // Método para retornar uma lista de sugestões de tarefas
    fun getTaskSuggestions(): List<RoutineTask> {
        // Aqui você pode retornar uma lista estática ou buscar do Supabase
        return listOf(
            RoutineTask(
                id = null,
                metaDataId = 1,
                name = "Organizar geladeira",
                description = "Lembre-se de limpar a geladeira toda semana para evitar consumo excessivo de energia",
                area = "Energia",
                complete = false,
                date = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
            ),
            RoutineTask(
                id = null,
                metaDataId = 2,
                name = "Lavar carro com balde",
                description = "Use um balde em vez da mangueira para economizar água",
                area = "Água",
                complete = false,
                date = kotlinx.datetime.Clock.System.now().toLocalDateTime(kotlinx.datetime.TimeZone.currentSystemDefault()).date
            )
        )
    }

    // Método para adicionar uma tarefa à rotina
    fun addTaskToRoutine(task: RoutineTask) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                // Verifica se a rotina já existe
                val routine = _currentRoutine.value ?: Routine(
                    id = null,
                    userId = 1, // Substitua pelo ID do usuário atual
                    taskList = emptyList()
                )

                // Adiciona a nova tarefa à rotina
                val updatedTaskList = routine.taskList + task
                val updatedRoutine = routine.copy(taskList = updatedTaskList)

                // Atualiza o estado da rotina
                _currentRoutine.value = updatedRoutine

                // Aqui você pode adicionar a lógica para salvar a rotina no Supabase
                // saveRoutineToSupabase(updatedRoutine)

                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }

    // Método para atualizar uma tarefa como completa
    fun updateTaskAsComplete(taskId: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val routine = _currentRoutine.value ?: throw IllegalStateException("Rotina não encontrada")

                // Encontra a tarefa e a marca como completa
                val updatedTaskList = routine.taskList.map { task ->
                    if (task.id == taskId) task.copy(complete = true) else task
                }

                val updatedRoutine = routine.copy(taskList = updatedTaskList)

                // Atualiza o estado da rotina
                _currentRoutine.value = updatedRoutine

                // Aqui você pode adicionar a lógica para salvar a rotina no Supabase
                // saveRoutineToSupabase(updatedRoutine)

                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = e.message
                _isLoading.value = false
            }
        }
    }
}
package io.github.sustainow.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.github.sustainow.domain.model.Routine
import io.github.sustainow.domain.model.RoutineTask
import io.github.sustainow.domain.model.RoutineTaskMetaData
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import java.util.Date

class RoutineViewModel : ViewModel() {

    // Estado da rotina atual do usuário
    private val _currentRoutine = MutableLiveData<Routine?>()
    val currentRoutine: LiveData<Routine?> get() = _currentRoutine

    // Lista de sugestões de tarefas padrão
    private val defaultSuggestions = listOf(
        RoutineTaskMetaData(
            id = 1,
            routineId = 0,
            name = "Desligar as luzes ao sair",
            description = "Reduza o consumo de energia desligando luzes desnecessárias.",
            area = "Energia",
            weekdays = listOf(1,2,3)
        ),
        RoutineTaskMetaData(
            id = 2,
            routineId = 0,
            name = "Reduzir o tempo do banho",
            description = "Economize água ao limitar o tempo do banho para menos de 10 minutos.",
            area = "Água",
            weekdays = listOf(0, 1, 2, 3, 4, 5, 6)
        )
    )


    fun getTaskSuggestions(): List<RoutineTaskMetaData> {
        return defaultSuggestions
    }


    fun addTaskToRoutine(taskMetadata: RoutineTaskMetaData) {
        val current = _currentRoutine.value
        val newTask = taskMetadata.id?.let {
            RoutineTask(
                id = null,
                metaDataId = it,
                name = taskMetadata.name,
                description = taskMetadata.description,
                area = taskMetadata.area,
                complete = false,
                date = LocalDate(2000,1,1)
            )
        }

        if (current == null) {
            // Inicializa a rotina e adiciona a tarefa
            if (newTask != null) {
                _currentRoutine.value = Routine(
                    id = newTask.id,
                    userId = 0,
                    taskList = mutableListOf(newTask)
                )
            }
        } else {
            // Adiciona a tarefa à rotina existente
            val updatedActivities = current.taskList.toMutableList().apply {
                if (newTask != null) {
                    add(newTask)
                }
            }
            _currentRoutine.value = current.copy(taskList = updatedActivities)
        }
    }


    fun markTaskComplete(instanceId: Int) {
        val current = _currentRoutine.value ?: return
        val updatedTask = current.taskList.map { task ->
            if (task.id == instanceId) {
                task.copy(complete = true)
            } else {
                task
            }
        }
        _currentRoutine.value = current.copy(taskList = updatedTask)
    }
}

package io.github.sustainow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import io.github.sustainow.domain.model.RoutineTaskMetaData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CreateTaskViewModel : ViewModel() {
    private val _taskMetaData = MutableStateFlow(RoutineTaskMetaData(
        routineId = 0,
        name = "",
        description = "",
        area = "",
        weekdays = emptyList()
    ))
    val taskMetaData: StateFlow<RoutineTaskMetaData> = _taskMetaData

    fun updateName(name: String) {
        _taskMetaData.value = _taskMetaData.value.copy(name = name)
    }

    fun updateDescription(description: String) {
        _taskMetaData.value = _taskMetaData.value.copy(description = description)
    }

    fun updateArea(area: String) {
        _taskMetaData.value = _taskMetaData.value.copy(area = area)
    }

    fun updateWeekdays(weekdays: List<Int>) {
        _taskMetaData.value = _taskMetaData.value.copy(weekdays = weekdays)
    }

    fun saveTask() {
        // Aqui você pode adicionar a lógica para salvar a tarefa no Supabase
    }
}
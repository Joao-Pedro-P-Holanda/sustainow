package io.github.sustainow.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import io.github.sustainow.domain.model.Routine
import io.github.sustainow.domain.model.RoutineTask
import io.github.sustainow.domain.model.RoutineTaskMetaData
import kotlinx.datetime.LocalDate
import javax.inject.Inject


@HiltViewModel
class RoutineViewModel @Inject constructor(): ViewModel() {

    // Estado da rotina atual do usuário usando StateFlow
    private val _currentRoutine = MutableStateFlow<Routine?>(null)
    val currentRoutine: StateFlow<Routine?> get() = _currentRoutine


    private val mockRoutine = Routine(
        id = 1,
        userId = 0,
        taskList = mutableListOf(
            RoutineTask(
                id = 1,
                metaDataId = 1,
                name = "Desligar as luzes ao sair",
                description = "Reduza o consumo de energia desligando luzes desnecessárias.",
                area = "Energia",
                complete = false,
                date = LocalDate(2023, 6, 1)
            ),
            RoutineTask(
                id = 2,
                metaDataId = 2,
                name = "Reduzir o tempo do banho",
                description = "Economize água ao limitar o tempo do banho para menos de 10 minutos.",
                area = "Água",
                complete = false,
                date = LocalDate(2023, 6, 1)
            )
        )
    )

    init {

        _currentRoutine.value = mockRoutine
        Log.d("RoutineViewModel", "Rotina definida: ${_currentRoutine.value}")
    }
}

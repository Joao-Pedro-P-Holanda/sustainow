package io.github.sustainow.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.domain.model.ConsumptionTotal
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.UserState
import io.github.sustainow.presentation.ui.utils.DataError
import io.github.sustainow.presentation.ui.utils.DataOperation
import io.github.sustainow.presentation.ui.utils.getFirstDayOfCurrentMonth
import io.github.sustainow.presentation.ui.utils.getFirstDayOfPreviousMonth
import io.github.sustainow.presentation.ui.utils.getLastDayOfCurrentMonth
import io.github.sustainow.presentation.ui.utils.getLastDayOfPreviousMonth
import io.github.sustainow.presentation.ui.utils.toLocalDate
import io.github.sustainow.repository.formulary.FormularyRepository
import io.github.sustainow.service.auth.AuthService
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.withTimeout
import kotlinx.datetime.LocalDate

import javax.inject.Inject

@HiltViewModel
class HomeViewModel
@Inject
constructor(
    private val authService: AuthService,
    private val repository: FormularyRepository,
) : ViewModel() {

    private val _carbonFootprintCurrent = MutableStateFlow<ConsumptionTotal?>(null)
    val carbonFootprintCurrent = _carbonFootprintCurrent.asStateFlow()

    private val _carbonFootprintDate = MutableStateFlow<LocalDate?>(null)
    val carbonFootprintDate = _carbonFootprintDate.asStateFlow()

    private val _energyConsumeCurrent = MutableStateFlow<ConsumptionTotal?>(null)
    val energyConsumeCurrent = _energyConsumeCurrent.asStateFlow()

    private val _energyConsumePrevious = MutableStateFlow<ConsumptionTotal?>(null)
    val energyConsumePrevious = _energyConsumePrevious.asStateFlow()

    private val _waterConsumeCurrent = MutableStateFlow<ConsumptionTotal?>(null)
    val waterConsumeCurrent = _waterConsumeCurrent.asStateFlow()

    private val _waterConsumePrevious = MutableStateFlow<ConsumptionTotal?>(null)
    val waterConsumePrevious = _waterConsumePrevious.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<DataError?>(null)
    val error = _error.asStateFlow()

    init {
        // Chama a função suspensa dentro de uma coroutine
        viewModelScope.launch {
            fetchConsumptionValues()
        }
    }

    private suspend fun getDefaultIfEmpty(answers: List<FormularyAnswer>, default: Float, unit: String): ConsumptionTotal {
        return if (answers.isEmpty()) {
            // Retorna um valor default quando não houver respostas
            ConsumptionTotal(default, unit)
        } else {
            // Caso haja respostas, calcula o total
            repository.getTotal(answers)
        }
    }

    private suspend fun fetchConsumptionValues() {
        viewModelScope.launch {
            _loading.value = true
            try {
                if (authService.user.value is UserState.Logged) {
                    val currentStartDate = getFirstDayOfCurrentMonth()
                    val currentEndDate = getLastDayOfCurrentMonth()
                    val previousStartDate = getFirstDayOfPreviousMonth()
                    val previousEndDate = getLastDayOfPreviousMonth()

                    val results = coroutineScope {
                        listOf(
                            async { "carbon_footprint" to fetchWithTimeout(emptyList<FormularyAnswer>()) { repository.getAnswered("carbon_footprint", "expected", currentStartDate, currentEndDate) ?: emptyList() } },
                            async { "energy_current" to fetchWithTimeout(emptyList<FormularyAnswer>()) { repository.getAnswered("energy_consumption", "real", currentStartDate, currentEndDate) ?: emptyList() } },
                            async { "energy_previous" to fetchWithTimeout(emptyList<FormularyAnswer>()) { repository.getAnswered("energy_consumption", "real", previousStartDate, previousEndDate) ?: emptyList() } },
                            async { "water_current" to fetchWithTimeout(emptyList<FormularyAnswer>()) { repository.getAnswered("water_consumption", "real", currentStartDate, currentEndDate) ?: emptyList() } },
                            async { "water_previous" to fetchWithTimeout(emptyList<FormularyAnswer>()) { repository.getAnswered("water_consumption", "real", previousStartDate, previousEndDate) ?: emptyList() } }
                        ).awaitAll()
                    }

                    // Processamento dos resultados
                    val carbonAnswersCurrent = results.find { it.first == "carbon_footprint" }?.second ?: emptyList()
                    val energyAnswersCurrent = results.find { it.first == "energy_current" }?.second ?: emptyList()
                    val energyAnswersPrevious = results.find { it.first == "energy_previous" }?.second ?: emptyList()
                    val waterAnswersCurrent = results.find { it.first == "water_current" }?.second ?: emptyList()
                    val waterAnswersPrevious = results.find { it.first == "water_previous" }?.second ?: emptyList()

                    // Ajustando para passar valores default quando não houver respostas
                    _carbonFootprintCurrent.value = getDefaultIfEmpty(carbonAnswersCurrent, 0.0f, "kg")
                    _carbonFootprintDate.value = carbonAnswersCurrent.lastOrNull()?.answerDate?.toLocalDate()

                    _energyConsumeCurrent.value = getDefaultIfEmpty(energyAnswersCurrent, 0.0f, "kWh")
                    _energyConsumePrevious.value = getDefaultIfEmpty(energyAnswersPrevious, 0.0f, "kWh")

                    _waterConsumeCurrent.value = getDefaultIfEmpty(waterAnswersCurrent, 0.0f, "m³")
                    _waterConsumePrevious.value = getDefaultIfEmpty(waterAnswersPrevious, 0.0f, "m³")

                    _error.value = null
                } else {
                    _error.value = DataError(source = "home", operation = DataOperation.GET)
                }
            } catch (e: Exception) {
                _error.value = DataError(source = "home", operation = DataOperation.GET, message = e.message)
            } finally {
                _loading.value = false
            }
        }
    }


    private suspend fun <T> fetchWithTimeout(defaultValue: T, block: suspend () -> T): T {
        return runCatching {
            withTimeout(7000) {
                block() ?: defaultValue
            }
        }.getOrElse { e ->
            defaultValue
        }
    }
}
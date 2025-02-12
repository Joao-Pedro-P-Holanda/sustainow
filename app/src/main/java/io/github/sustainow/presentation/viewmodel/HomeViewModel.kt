package io.github.sustainow.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.sustainow.domain.model.ConsumptionTotal
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
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

    private val _carbonFootprintPrevious = MutableStateFlow<ConsumptionTotal?>(null)
    val carbonFootprintPrevious = _carbonFootprintPrevious.asStateFlow()

    private val _carbonFootprintDate = MutableStateFlow<LocalDate?>(null)
    val carbonFootprintDate = _carbonFootprintDate.asStateFlow()

    private val _energyConsumeCurrent = MutableStateFlow<ConsumptionTotal?>(null)
    val energyConsumeCurrent = _energyConsumeCurrent.asStateFlow()

    private val _energyConsumePrevious = MutableStateFlow<ConsumptionTotal?>(null)
    val energyConsumePrevious = _energyConsumePrevious.asStateFlow()

    private val _energyConsumeDate = MutableStateFlow<LocalDate?>(null)
    val energyConsumeDate = _energyConsumeDate.asStateFlow()

    private val _waterConsumeCurrent = MutableStateFlow<ConsumptionTotal?>(null)
    val waterConsumeCurrent = _waterConsumeCurrent.asStateFlow()

    private val _waterConsumePrevious = MutableStateFlow<ConsumptionTotal?>(null)
    val waterConsumePrevious = _waterConsumePrevious.asStateFlow()

    private val _waterConsumeDate = MutableStateFlow<LocalDate?>(null)
    val waterConsumeDate = _waterConsumeDate.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<DataError?>(null)
    val error = _error.asStateFlow()

    init {
        fetchConsumptionValues()
    }

    private fun fetchConsumptionValues() {
        viewModelScope.launch {
            _loading.value = true
            try {
                if (authService.user.value is UserState.Logged) {
                    val currentStartDate = getFirstDayOfCurrentMonth()
                    val currentEndDate = getLastDayOfCurrentMonth()
                    val previousStartDate = getFirstDayOfPreviousMonth()
                    val previousEndDate = getLastDayOfPreviousMonth()

                    // Obtendo as respostas de consumo
                    val carbonAnswersCurrent = repository.getAnswered("carbon_footprint", "expected", currentStartDate, currentEndDate)
                    val carbonAnswersPrevious = repository.getAnswered("carbon_footprint", "expected", previousStartDate, previousEndDate)

                    val energyAnswersCurrent = repository.getAnswered("energy_consume", "real", currentStartDate, currentEndDate)
                    val energyAnswersPrevious = repository.getAnswered("energy_consume", "real", previousStartDate, previousEndDate)

                    val waterAnswersCurrent = repository.getAnswered("water_consume", "real", currentStartDate, currentEndDate)
                    val waterAnswersPrevious = repository.getAnswered("water_consume", "real", previousStartDate, previousEndDate)
                    Log.d("HomeViewModel", "Carbon Answers Current: $carbonAnswersCurrent")

                    // Obtendo totais de consumo
                    val totalCarbonCurrent = repository.getTotal(carbonAnswersCurrent)
                    val totalCarbonPrevious = repository.getTotal(carbonAnswersPrevious)

                    val totalEnergyCurrent = repository.getTotal(energyAnswersCurrent)
                    val totalEnergyPrevious = repository.getTotal(energyAnswersPrevious)

                    val totalWaterCurrent = repository.getTotal(waterAnswersCurrent)
                    val totalWaterPrevious = repository.getTotal(waterAnswersPrevious)

                    // Atribuindo os valores aos StateFlow
                    _carbonFootprintCurrent.value = totalCarbonCurrent
                    _carbonFootprintPrevious.value = totalCarbonPrevious
                    _carbonFootprintDate.value = carbonAnswersCurrent.lastOrNull()?.answerDate?.toLocalDate()

                    _energyConsumeCurrent.value = totalEnergyCurrent
                    _energyConsumePrevious.value = totalEnergyPrevious
                    _energyConsumeDate.value = energyAnswersCurrent.lastOrNull()?.answerDate?.toLocalDate()

                    _waterConsumeCurrent.value = totalWaterCurrent
                    _waterConsumePrevious.value = totalWaterPrevious
                    _waterConsumeDate.value = waterAnswersCurrent.lastOrNull()?.answerDate?.toLocalDate()

                    _error.value = null
                } else {
                    _error.value = DataError(source = "home", operation = DataOperation.GET)
                }
            } catch (e: Exception) {
                _error.value = DataError(source = "home", operation = DataOperation.GET, message = "Erro ao carregar respostas dos formulários")
            } finally {
                _loading.value = false
            }
        }
    }
}

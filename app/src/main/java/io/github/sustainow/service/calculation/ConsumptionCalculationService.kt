package io.github.sustainow.service.calculation

import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.TotalConsumption

interface ConsumptionCalculationService {
    /**
     * Receives a list of answers and calculates a total value in the specified unit
     * @throws UnsupportedOperationException when conversion to the given unit is not possible
     */
    suspend fun calculateTotal(
        answers: List<FormularyAnswer>,
        unit: String,
    ): TotalConsumption
}

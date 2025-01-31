package io.github.sustainow.service.calculation

import io.github.sustainow.domain.model.ConsumptionTotal
import io.github.sustainow.domain.model.FormularyAnswer

interface CalculationService {
    suspend fun getTotal(answers:Iterable<FormularyAnswer>): ConsumptionTotal
}
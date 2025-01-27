package io.github.sustainow.service.calculation

import io.github.sustainow.domain.model.ConsumptionTotal
import io.github.sustainow.domain.model.FormularyAnswer
import retrofit2.http.GET
import retrofit2.http.POST

interface CalculationServiceGemini : CalculationService {
    @POST("gemini-1.5-flash:generateContent")
    override suspend fun getTotal(answers: Iterable<FormularyAnswer>): ConsumptionTotal

}
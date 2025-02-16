package io.github.sustainow.repository.formulary

import io.github.sustainow.domain.model.ConsumptionTotal
import io.github.sustainow.domain.model.Formulary
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.FormularyAnswerCreate
import kotlinx.datetime.LocalDate

/**
 * Repository to handle formulary data
 */
interface FormularyRepository {
    suspend fun getFormulary(
        area: String,
        type: String,
    ): Formulary

    /**
     * Gets the formulary answers of an user in a given time period
     */
    suspend fun getAnswered(
        area: String,
        type: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<FormularyAnswer>

    suspend fun reuseAnswered(
        area: String,
        type: String,
        startDate: LocalDate,
        endDate: LocalDate,
    ): List<FormularyAnswerCreate>

    suspend fun getTotal(answers: Iterable<FormularyAnswer>): ConsumptionTotal

    /**
     * Adds new formulary answers made by an user
     */
    suspend fun addAnswers(
        answers: List<FormularyAnswerCreate>,
        userId: String,
        formId: Int,
    ): List<FormularyAnswer>

    /**
     * Updates a formulary with new answers
     */
    suspend fun updateAnswers(
        answers: List<FormularyAnswer>,
        userId: String,
    ): List<FormularyAnswer>
}

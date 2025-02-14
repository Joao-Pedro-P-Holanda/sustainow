package io.github.sustainow.repository.formulary

import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.sustainow.domain.model.ConsumptionTotal
import io.github.sustainow.domain.model.Formulary
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.exceptions.ResponseException
import io.github.sustainow.exceptions.TimeoutException
import io.github.sustainow.exceptions.UnknownException
import io.github.sustainow.repository.mapper.SupabaseMapper
import io.github.sustainow.repository.model.SerializableFormulary
import io.github.sustainow.repository.model.SerializableFormularyAnswer
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.datetime.LocalDate
import javax.inject.Inject

import io.github.sustainow.service.calculation.CalculationService
class FormularyRepositorySupabaseImp
    @Inject
    constructor(
        private val supabase: SupabaseClient,
        private val calculationService: CalculationService,
        private val formularyTableName: String,
        private val answerTableName: String,
        private val questionTableName: String,
        private val questionDependencyTableName: String,
        private val alternativeTableName: String,
    ) : FormularyRepository {
        private val mapper = SupabaseMapper()

        override suspend fun getFormulary(
            area: String,
            type: String,
        ): Formulary {
            try {
                val response =
                    supabase.from(
                        formularyTableName,
                    ).select(
                        Columns.raw(
                            """
                            id, area, type,
                            $questionTableName(
                                id, name, text, type,
                                $alternativeTableName(id, id_question, area, name, text, value, time_period, unit), 
                                $questionDependencyTableName!${questionDependencyTableName}_id_dependant_fkey(
                                    dependency_expression, 
                                    id_dependant,
                                    id_required_question
                                    )
                                )
                            )
                            """.trimIndent(),
                        ),
                    ) {
                        filter {
                            eq("area", area)
                            eq("type", type)
                        }
                        order(column = "id", order = Order.ASCENDING, referencedTable = questionTableName)
                    }.decodeSingle<SerializableFormulary>()
                return mapper.toDomain(response)
            } catch (e: RestException) {
                throw ResponseException("Error getting formulary", e)
            } catch (e: HttpRequestException) {
                throw UnknownException("Server error", e)
            } catch (e: HttpRequestTimeoutException) {
                throw TimeoutException("Timeout exception", e)
            } catch (e: Exception) {
                throw e
            }
        }

        override suspend fun getAnswered(
            area: String,
            type: String,
            startDate: LocalDate,
            endDate: LocalDate,
        ): List<FormularyAnswer> {
            try {
                val response =
                    supabase.from(
                        answerTableName,
                    ).select(
                        Columns.raw(
                            """
                            id, form_id, user_id, question_id, value, time_period, unit, answer_date, month, group_name,type,
                            $formularyTableName(
                                area
                                )
                            """.trimIndent(),
                        ),
                    ) {
                        filter {
                            and {
                                eq("area", area)
                                eq("type", type)
                                gte("answer_date", startDate)
                                lte("answer_date", endDate)
                            }
                        }
                    }.decodeAs<List<SerializableFormularyAnswer>>()
                val converted = response.map { mapper.toDomain(it) }
                Log.i("SupabaseRepository", converted.toString())
                return converted
            } catch (e: RestException) {
                throw ResponseException("Error getting response for formulary answers", e)
            } catch (e: HttpRequestException) {
                throw UnknownException("Server error", e)
            } catch (e: HttpRequestTimeoutException) {
                throw TimeoutException("Timeout exception", e)
            }
        }

    override suspend fun getTotal(answers: Iterable<FormularyAnswer>): ConsumptionTotal {
        val result = calculationService.getTotal(answers)
        Log.i("SupabaseRepository", result.toString())
        return result
    }

    override suspend fun addAnswers(
            answers: List<FormularyAnswer>,
            userId: String,
            formId: Int
        ): List<FormularyAnswer> {
            try {
                val newList = answers.map { answer ->
                    answer.copy(uid = userId, formId = formId)
                }

                val result =
                    supabase.from(answerTableName).insert(
                        newList.map { mapper.toSerializable(it) },
                    ) {
                        select()
                    }.decodeAs<List<SerializableFormularyAnswer>>()
                val converted = result.map { mapper.toDomain(it) }
                return converted
            } catch (e: RestException) {
                throw ResponseException("Error adding new formulary answers", e)
            } catch (e: HttpRequestException) {
                throw UnknownException("Server error", e)
            } catch (e: HttpRequestTimeoutException) {
                throw TimeoutException("Timeout exception", e)
            }
        }

        override suspend fun updateAnswers(
            answers: List<FormularyAnswer>,
            userId: String,
        ): List<FormularyAnswer> {
            try {
                val result =
                    supabase.from(answerTableName)
                        .update(answers.map { mapper.toSerializable(it) }) {
                            select()
                        }.decodeAs<List<SerializableFormularyAnswer>>()
                val converted = result.map { mapper.toDomain(it) }
                return converted
            } catch (e: RestException) {
                throw ResponseException("Error updating formulary answers", e)
            } catch (e: HttpRequestException) {
                throw UnknownException("Server error", e)
            } catch (e: HttpRequestTimeoutException) {
                throw TimeoutException("Timeout exception", e)
            }
        }
    }

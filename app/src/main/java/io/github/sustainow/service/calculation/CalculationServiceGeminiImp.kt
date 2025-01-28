package io.github.sustainow.service.calculation

import SerializableFormularyAnswerGeminiRequest
import FormularyAnswerList
import android.util.Log
import io.github.sustainow.BuildConfig
import io.github.sustainow.domain.model.ConsumptionTotal
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.repository.mapper.SupabaseMapper
import io.github.sustainow.service.model.SerializableConsumptionTotal
import io.github.sustainow.service.model.SerializableGeminiResponse
import io.github.sustainow.service.model.SerializableTotalConsumptionField
import io.github.sustainow.service.model.SerializableTotalConsumptionGenerationConfig
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import retrofit2.http.Body
import kotlinx.serialization.json.Json
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Inject

class CalculationServiceGeminiImp @Inject constructor(private val client: HttpClient) : CalculationService {
    private val mapper = SupabaseMapper()

    override suspend fun getTotal(answers: Iterable<FormularyAnswer>): ConsumptionTotal{
        try {
            val serializedList = FormularyAnswerList(answers.map { mapper.toSerializable(it) })
            val generationConfig = SerializableTotalConsumptionGenerationConfig(
                mimeType = "application/json",
                schema = SerializableTotalConsumptionField(
                    type = "object",
                    properties = mapOf(
                        "total" to SerializableTotalConsumptionField(
                            type = "number",
                        ),
                        "unit" to SerializableTotalConsumptionField(
                            type = "string",
                        )
                    )
                )
            )
            val serializedObjects  =SerializableFormularyAnswerGeminiRequest(serializedList, generationConfig)
            Log.i("CalculationServiceGeminiImp", "Json schema: ${Json{ prettyPrint=true }.encodeToString(SerializableFormularyAnswerGeminiRequest.serializer(), serializedObjects)}")

            val result = client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent"){
                    url{
                        parameters.append("key",BuildConfig.GEMINI_API_KEY)
                    }
                    contentType(ContentType.Application.Json)
                    setBody(serializedObjects)
            }.body<SerializableGeminiResponse>()
            Log.i("CalculationServiceGeminiImp", "getTotal: $result")

            val schemaResult = result.candidates.first().content.parts.first().text
            val serializedConsumptionTotal = Json.decodeFromString<SerializableConsumptionTotal>(schemaResult)
            return ConsumptionTotal(total=serializedConsumptionTotal.total,unit=serializedConsumptionTotal.unit)
        } catch (e: Exception) {
            Log.e("CalculationServiceGeminiImp", "getTotal: $e")
            throw e
        }
    }

}

interface CalculationServiceGeminiClient{
    @Headers("Content-Type: application/json")
    @POST("gemini-1.5-flash:generateContent")
    suspend fun getTotal(@Body requestBody: SerializableFormularyAnswerGeminiRequest): SerializableConsumptionTotal
}



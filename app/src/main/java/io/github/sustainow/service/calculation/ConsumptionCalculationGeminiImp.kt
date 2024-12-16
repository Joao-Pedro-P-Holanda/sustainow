package io.github.sustainow.service.calculation

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.Schema
import com.google.ai.client.generativeai.type.generationConfig
import io.github.sustainow.BuildConfig
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.TotalConsumption
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement

val schema =
    Schema.obj(
        "total_consumption",
        "Calculated consumption on a given unit from a list of answers",
        Schema.str("unit", "The measurement unit used"),
        Schema.double("total", "Total value measured for the unit"),
        Schema.arr(
            "steps",
            "detailed steps of each conversion/calculation made, used only for debugging",
            Schema.str("item", "Step items"),
        ),
    )

class ConsumptionCalculationGeminiImp : ConsumptionCalculationService {
    private lateinit var generativeModel: GenerativeModel

    init {
        println(BuildConfig.GEMINI_API_KEY)
        try {
            generativeModel =
                GenerativeModel(
                    "gemini-1.5-flash",
                    apiKey = BuildConfig.GEMINI_API_KEY,
                    generationConfig =
                        generationConfig {
                            responseMimeType = "application/json"
                            responseSchema = schema
                        },
                )
        } catch (e: Exception) {
            Log.e("ConsumptionCalculationGeminiImp", e.message ?: "")
        }
    }

    override suspend fun calculateTotal(
        answers: List<FormularyAnswer>,
        unit: String,
    ): TotalConsumption {
        try {
            val prompt = getTextForAnswers(answers, unit)
            Log.i("ConsumptionCalculationGeminiImp", "Calculating total on Gemini with prompt $prompt")
            val response = generativeModel.generateContent(prompt)
            check(response.text != null)
            val result = Json.parseToJsonElement(response.text!!)
            return Json.decodeFromJsonElement(result)
        } catch (e: Exception) {
            Log.e("ConsumptionCalculationGeminiImp", e.message ?: "", e)
            throw UnsupportedOperationException("Failed returning total in unit $unit", e)
        }
    }

    private fun getTextForAnswers(
        answers: List<FormularyAnswer>,
        unit: String,
    ): String {
        val prompt =
            listOf("Target $unit") +
                answers.map {
                        answer ->
                    "(${answer.value},${answer.unit},${answer.timePeriod?.toIsoString() ?: ""})"
                }
        return prompt.joinToString(separator = "\n")
    }
}

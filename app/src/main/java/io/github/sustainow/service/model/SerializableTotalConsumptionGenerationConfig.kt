package io.github.sustainow.service.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
@SerialName("generationConfig")
data class SerializableTotalConsumptionGenerationConfig (
    @SerialName("response_mime_type")
    val mimeType: String,
    @SerialName("response_schema")
    val schema: SerializableTotalConsumptionField
)

/**
 * Represents a field for Gemini schema, which can be nested
 * Every field should have their SerialName defined
 * */
@Serializable
data class SerializableTotalConsumptionField(
    val type: String,
    val properties: Map<String,SerializableTotalConsumptionField>?=null,
    val items: Map<String,SerializableTotalConsumptionField>?=null
)
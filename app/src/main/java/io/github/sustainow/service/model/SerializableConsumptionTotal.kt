package io.github.sustainow.service.model

import kotlinx.serialization.Serializable

@Serializable
data class SerializableGeminiResponse(
    val candidates:List<SerializableResponseCandidate>
)

@Serializable
data class SerializableResponseCandidate(
    val content:SerializableResponseContent
)
@Serializable
data class SerializableResponseContent(
    val parts: List<SerializableResponsePart>
)
@Serializable
data class SerializableResponsePart(
    val text:String
)


@Serializable
data class SerializableConsumptionTotal (
    val total: Float,
    val unit: String
)
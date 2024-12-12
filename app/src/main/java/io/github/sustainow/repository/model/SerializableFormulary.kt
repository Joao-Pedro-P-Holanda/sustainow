package io.github.sustainow.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SerializableFormulary(
    val id: Int? = null,
    val area: String,
    val type: String,
    @SerialName("question")
    val questions: List<SerializableQuestion>,
)

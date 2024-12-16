package io.github.sustainow.service.model

import kotlinx.serialization.Serializable

@Serializable
data class SerializableTotalConsumption(
    val total: Float,
    val unit: String,
    // used only for debugging the calculation
    val steps: List<String>? = null,
)

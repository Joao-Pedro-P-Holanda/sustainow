package io.github.sustainow.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class SerializableQuestionAlternative(
    val area: String,
    val name: String? = null, // only for multi-item questions, groups the alternatives
    val text: String,
    var value: Float, // updated in numerical and multi-field questions
    @SerialName("time_period")
    var timePeriod: Duration?, // updated in multi-field questions
    val unit: String,
)

package io.github.sustainow.repository.model

import kotlinx.serialization.Serializable


@Serializable
data class SerializableRoutineTaskMetaData(
    val id: Int? = null,
    val routineId: Int,
    val name: String,
    val description: String?,
    val area: String,
    val weekdays: List<Int>
)
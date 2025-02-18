package io.github.sustainow.repository.model

import kotlinx.serialization.Serializable

@Serializable
data class SerializableRoutine(
    val id: Int? = null,
    val userId: String,
    val taskList: List<SerializableRoutineTask>
)
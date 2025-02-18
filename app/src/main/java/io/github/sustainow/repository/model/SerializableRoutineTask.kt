package io.github.sustainow.repository.model


import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable

@Serializable
data class SerializableRoutineTask(
    val id: Int?,
    val metaDataId: Int,
    val name: String,
    val description: String?,
    val area: String,
    val complete: Boolean,
    val dueDate: String
)
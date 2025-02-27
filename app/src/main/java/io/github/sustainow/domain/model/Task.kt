package io.github.sustainow.domain.model

import kotlinx.datetime.LocalDate

data class Task (
    val id: Int,
    val name: String,
    val description: String,
    val routineId: Int,
    val metadataId: Int,
    val area: String,
    val complete: Boolean = false,
    val dueDate: LocalDate,

)
package io.github.sustainow.domain.model

import kotlinx.datetime.LocalDate

data class RoutineTask(
    val id: Int?,
    val metaDataId:Int,
    val name: String,
    val description: String?,
    val area: String,
    val complete: Boolean,
    val date: LocalDate
)

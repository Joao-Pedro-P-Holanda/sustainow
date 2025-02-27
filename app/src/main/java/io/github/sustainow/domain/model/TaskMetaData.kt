package io.github.sustainow.domain.model

import io.ktor.util.date.WeekDay

data class TaskMetaData(
    val id:String,
    val routineId: Int,
    val name:String,
    val description:String?,
    val area:String,
    val weekDay: WeekDay
)

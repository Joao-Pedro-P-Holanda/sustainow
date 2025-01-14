package io.github.sustainow.domain.model

import kotlinx.datetime.DayOfWeek

data class RoutineTaskMetaData(
    val id:Int? = null,
    val routineId:Int,
    val name:String,
    val description:String?,
    val area:String,
    val weekday: DayOfWeek
)
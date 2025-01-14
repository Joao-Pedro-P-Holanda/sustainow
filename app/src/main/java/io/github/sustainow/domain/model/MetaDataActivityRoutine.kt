package io.github.sustainow.domain.model

import kotlinx.datetime.DayOfWeek

data class MetaDataActivityRoutine(
    val id:Int,
    val routineId:Int,
    val name:String,
    val description:String?,
    val area:String,
    val weekday: DayOfWeek
)
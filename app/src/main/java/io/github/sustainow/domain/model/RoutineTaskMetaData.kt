package io.github.sustainow.domain.model

data class RoutineTaskMetaData(
    val id:Int? = null,
    val routineId:Int,
    val name:String,
    val description:String?,
    val area:String,
    val weekdays: List<Int>
)
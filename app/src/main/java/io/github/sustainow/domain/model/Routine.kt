package io.github.sustainow.domain.model

data class Routine(
    val id:Int? = null,
    val userId:Int,
    val taskList: List<RoutineTask>
)
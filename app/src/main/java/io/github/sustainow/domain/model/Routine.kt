package io.github.sustainow.domain.model

data class Routine(
    val id:Int? = null,
    val userId: String,
    val taskList: List<RoutineTask>
)
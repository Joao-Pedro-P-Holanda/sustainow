package io.github.sustainow.domain.model

data class Routine(
    val id:Int,
    val userId:Int,
    val activity_list:List<RoutineActivity>
)
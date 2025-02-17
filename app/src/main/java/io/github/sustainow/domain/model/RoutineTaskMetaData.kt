package io.github.sustainow.domain.model

//Esse modelo é usado na tela de criação para criar um modelo de tarefa que será realizada toda semana nessa rotina
data class RoutineTaskMetaData(
    val id:Int? = null,
    val routineId:Int,
    val name:String,
    val description:String?,
    val area:String,
    val weekdays: List<Int>
)
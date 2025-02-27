package io.github.sustainow.domain.model


data class TaskMetaData(
    val id:String,
    val routineId: Int,
    val name:String,
    val description:String?,
    val area:String,
    val weekDay: WeekDay
)

enum class WeekDay {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
}

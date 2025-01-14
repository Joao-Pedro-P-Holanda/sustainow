package io.github.sustainow.domain.model

import kotlinx.datetime.DayOfWeek
import java.util.Date

data class RoutineActivity(
    val id:Int,
    val name:String,
    val description:String?,
    val area:String,
    val complete:Boolean,
    val date:Date
)

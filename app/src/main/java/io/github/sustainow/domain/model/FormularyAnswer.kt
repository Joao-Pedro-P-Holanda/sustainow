package io.github.sustainow.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

class FormularyAnswer(
    val id: Int? = null,
    val formId: Int? = null,
    val uid: String,
    val area: String,
    val groupName: String,
    val question: Question,
    val value: Float,
    val unit: String,
    val answerDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val month: Int,
)

package io.github.sustainow.domain.model

import kotlinx.datetime.Instant
import kotlin.time.Duration

data class FormularyAnswer(
    val id: Int,
    val formId: Int,
    val uid: String,
    val groupName: String? = null,
    val questionId: Int,
    val value: Float,
    val timePeriod: Duration? = null,
    val unit: String,
    val answerDate: Instant,
    val type: String,
)

data class FormularyAnswerCreate(
    val text: String? = null,
    val formId: Int,
    val uid: String,
    val groupName: String? = null,
    val questionId: Int,
    val value: Float,
    val timePeriod: Duration? = null,
    val unit: String,
)

package io.github.sustainow.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

data class FormularyAnswer(
    var id: Int? = null,
    val text: String? = null,
    val formId: Int? = null,
    val uid: String? = null,
    val groupName: String? = null,
    val questionId: Int? = null,
    val value: Float,
    val timePeriod: Duration? = null,
    val unit: String,
    val answerDate: Instant = Clock.System.now(),
    val type: String? = null,
    val month: Int? = null,
)

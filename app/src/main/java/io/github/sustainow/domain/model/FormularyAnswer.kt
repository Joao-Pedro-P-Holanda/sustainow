package io.github.sustainow.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration

class FormularyAnswer(
    var id: Int? = null,
    val formId: Int? = null,
    val uid: String,
    val groupName: String,
    val questionId: Int? = null,
    val value: Float,
    val timePeriod: Duration? = null,
    val unit: String,
    val answerDate: Instant = Clock.System.now(),
    val month: Int,
)

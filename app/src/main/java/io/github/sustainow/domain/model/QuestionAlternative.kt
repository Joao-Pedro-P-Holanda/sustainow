package io.github.sustainow.domain.model

import kotlin.time.Duration

data class QuestionAlternative(
    val area: String,
    val name: String? = null, // only for multi-item questions, groups the alternatives
    val text: String,
    var value: Float, // updated in numerical and multi-field questions
    var timePeriod: Duration, // updated in multi-field questions
    val unit: String,
)

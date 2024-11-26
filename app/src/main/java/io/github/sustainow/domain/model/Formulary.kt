package io.github.sustainow.domain.model

import kotlinx.datetime.LocalDate

data class Formulary(
    val area: String,
    val answerDate: LocalDate? = null,
    val questions: List<Question>,
    val answers: MutableList<QuestionAlternative>,
    var total: Float,
    val unit: String,
)

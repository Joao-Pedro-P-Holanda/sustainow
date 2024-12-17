package io.github.sustainow.domain.model

data class Formulary(
    val id: Int? = null,
    val area: String,
    val type: String,
    val questions: List<Question>,
)

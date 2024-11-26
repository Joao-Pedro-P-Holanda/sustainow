package io.github.sustainow.domain.model

sealed class Question(
    area: String,
    name: String,
    text: String,
    alternatives: List<QuestionAlternative>,
    dependencies: List<QuestionAlternative>, // maps a question to the alternatives it needs from other questions
) {
    data class SingleSelect(
        val area: String,
        val name: String,
        val text: String,
        val alternatives: List<QuestionAlternative>,
        val dependencies: List<QuestionAlternative>,
    ) : Question(area, name, text, alternatives, dependencies)

    data class MultiSelect(
        val area: String,
        val name: String,
        val text: String,
        val alternatives: List<QuestionAlternative>,
        val dependencies: List<QuestionAlternative>,
    ) : Question(area, name, text, alternatives, dependencies)

    data class Numerical(
        val area: String,
        val name: String,
        val text: String,
        val alternatives: List<QuestionAlternative>,
        val dependencies: List<QuestionAlternative>,
    ) : Question(area, name, text, alternatives, dependencies)

    data class MultiItem(
        val area: String,
        val name: String,
        val text: String,
        val alternatives: MutableList<QuestionAlternative>,
        val dependencies: List<QuestionAlternative>,
    ) : Question(area, name, text, alternatives, dependencies)
}

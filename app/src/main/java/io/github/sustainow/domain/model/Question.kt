package io.github.sustainow.domain.model

sealed class Question(
    open val id: Int? = null,
    open val name: String?,
    open val text: String,
    open val alternatives: List<QuestionAlternative>,
    open val dependencies: List<QuestionDependency>, // maps a question to the expression required
) {
    data class SingleSelect(
        override val id: Int? = null,
        override val name: String?,
        override val text: String,
        override val alternatives: List<QuestionAlternative>,
        override val dependencies: List<QuestionDependency>,
    ) : Question(id, name, text, alternatives, dependencies)

    data class MultiSelect(
        override val id: Int? = null,
        override val name: String?,
        override val text: String,
        override val alternatives: List<QuestionAlternative>,
        override val dependencies: List<QuestionDependency>,
    ) : Question(id, name, text, alternatives, dependencies)

    data class Numerical(
        override val id: Int? = null,
        override val name: String?,
        override val text: String,
        override val alternatives: List<QuestionAlternative>,
        override val dependencies: List<QuestionDependency>,
    ) : Question(id, name, text, alternatives, dependencies)

    data class MultiItem(
        override val id: Int? = null,
        override var name: String?,
        override val text: String,
        override val alternatives: MutableList<QuestionAlternative>,
        override val dependencies: List<QuestionDependency>,
    ) : Question(id, name, text, alternatives, dependencies)
}

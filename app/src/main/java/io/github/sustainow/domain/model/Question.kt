package io.github.sustainow.domain.model

sealed class Question(
    open val id: Int,
    open val name: String?,
    open val text: String,
    open val alternatives: List<FormularyAnswerCreate>,
    open val dependencies: List<QuestionDependency>,
) {
    abstract fun onAnswerAdded(
        formAnswer: FormularyAnswerCreate,
        currentAnswers: List<FormularyAnswerCreate>,
    ): List<FormularyAnswerCreate>

    open fun onAnswerRemoved(
        formAnswer: FormularyAnswerCreate,
        currentAnswers: List<FormularyAnswerCreate>,
    ): List<FormularyAnswerCreate> = currentAnswers.filter { it != formAnswer }

    data class SingleSelect(
        override val id: Int,
        override val name: String?,
        override val text: String,
        override val alternatives: List<FormularyAnswerCreate>,
        override val dependencies: List<QuestionDependency>,
    ) : Question(id, name, text, alternatives, dependencies) {
        override fun onAnswerAdded(
            formAnswer: FormularyAnswerCreate,
            currentAnswers: List<FormularyAnswerCreate>,
        ): List<FormularyAnswerCreate> {
            val newList: List<FormularyAnswerCreate> =
                listOf(formAnswer).map { answer ->
                    answer.copy(questionId = this.id)
                }

            return newList
        }
    }

    data class MultiSelect(
        override val id: Int,
        override val name: String?,
        override val text: String,
        override val alternatives: List<FormularyAnswerCreate>,
        override val dependencies: List<QuestionDependency>,
    ) : Question(id, name, text, alternatives, dependencies) {
        override fun onAnswerAdded(
            formAnswer: FormularyAnswerCreate,
            currentAnswers: List<FormularyAnswerCreate>,
        ): List<FormularyAnswerCreate> {
            if (!currentAnswers.any {
                    it == formAnswer.copy(text = it.text)
                }
            ) {
                val newList = currentAnswers + formAnswer

                return newList
            }
            return currentAnswers
        }
    }

    data class Numerical(
        override val id: Int,
        override val name: String?,
        override val text: String,
        override val alternatives: List<FormularyAnswerCreate>,
        override val dependencies: List<QuestionDependency>,
    ) : Question(id, name, text, alternatives, dependencies) {
        override fun onAnswerAdded(
            formAnswer: FormularyAnswerCreate,
            currentAnswers: List<FormularyAnswerCreate>,
        ): List<FormularyAnswerCreate> {
            val newList: List<FormularyAnswerCreate> =
                listOf(formAnswer).map { answer ->
                    answer.copy(questionId = this.id)
                }

            return newList
        }
    }

    data class MultiItem(
        override val id: Int,
        override var name: String?,
        override val text: String,
        override val alternatives: MutableList<FormularyAnswerCreate>,
        override val dependencies: List<QuestionDependency>,
    ) : Question(id, name, text, alternatives, dependencies) {
        override fun onAnswerAdded(
            formAnswer: FormularyAnswerCreate,
            currentAnswers: List<FormularyAnswerCreate>,
        ): List<FormularyAnswerCreate> {
            val newList = currentAnswers + formAnswer

            return newList.map { answer ->
                answer.copy(questionId = this.id)
            }
        }
    }
}

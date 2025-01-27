package io.github.sustainow.domain.model

sealed class Question(
    open val id: Int? = null,
    open val name: String?,
    open val text: String,
    open val alternatives: List<FormularyAnswer>,
    open val dependencies: List<Pair<Int, String>>,
) {
    abstract fun onAnswerAdded(
        formAnswer: FormularyAnswer,
        currentAnswers: List<FormularyAnswer>
    ): List<FormularyAnswer>

    open fun onAnswerRemoved(
        formAnswer: FormularyAnswer,
        currentAnswers: List<FormularyAnswer>
    ): List<FormularyAnswer>{
        return currentAnswers.filter { it != formAnswer }
    }

    data class SingleSelect(
        override val id: Int? = null,
        override val name: String?,
        override val text: String,
        override val alternatives: List<FormularyAnswer>,
        override val dependencies: List<Pair<Int, String>>,
    ) : Question(id, name, text, alternatives, dependencies){
        override fun onAnswerAdded(
            formAnswer: FormularyAnswer,
            currentAnswers: List<FormularyAnswer>
        ): List<FormularyAnswer> {
            return listOf(formAnswer)
        }
    }

    data class MultiSelect(
        override val id: Int? = null,
        override val name: String?,
        override val text: String,
        override val alternatives: List<FormularyAnswer>,
        override val dependencies: List<Pair<Int, String>>,
    ) : Question(id, name, text, alternatives, dependencies){
        override fun onAnswerAdded(
            formAnswer: FormularyAnswer,
            currentAnswers: List<FormularyAnswer>
        ): List<FormularyAnswer> {
            return if(currentAnswers.contains(formAnswer)){
                currentAnswers
            }
            else{
                currentAnswers + formAnswer
            }
        }
    }

    data class Numerical(
        override val id: Int? = null,
        override val name: String?,
        override val text: String,
        override val alternatives: List<FormularyAnswer>,
        override val dependencies: List<Pair<Int, String>>,
    ) : Question(id, name, text, alternatives, dependencies){
        override fun onAnswerAdded(
            formAnswer: FormularyAnswer,
            currentAnswers: List<FormularyAnswer>
        ): List<FormularyAnswer> {
            return listOf(formAnswer)
        }
    }

    data class MultiItem(
        override val id: Int? = null,
        override var name: String?,
        override val text: String,
        override val alternatives: MutableList<FormularyAnswer>,
        override val dependencies: List<Pair<Int, String>>,
    ) : Question(id, name, text, alternatives, dependencies){
        override fun onAnswerAdded(
            formAnswer: FormularyAnswer,
            currentAnswers: List<FormularyAnswer>
        ): List<FormularyAnswer> {
            return currentAnswers + formAnswer
        }
    }
}

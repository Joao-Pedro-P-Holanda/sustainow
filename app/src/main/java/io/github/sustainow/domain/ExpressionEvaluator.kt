package io.github.sustainow.domain

import android.util.Log
import io.github.sustainow.domain.model.FormularyAnswerCreate
import io.github.sustainow.domain.model.QuestionDependency

fun dependenciesSatisfied(
    dependencies: List<QuestionDependency>,
    answers: List<FormularyAnswerCreate>,
): Boolean {
    Log.i("ExpressionEvaluator", "Evaluating dependencies $dependencies")
    return dependencies.all { dependency ->
        val operators = dependency.dependencyExpression.split(" ")
        val questionAnswers =
            answers.filter {
                it.questionId == dependency.idRequiredQuestion
            }
        return questionAnswers.all {
            val comparison =
                Comparison(
                    left = it.value.toDouble(),
                    right = operators[2].toDouble(),
                    operator = operators[1],
                )
            comparison.evaluate()
        }
    }
}

data class Comparison(
    val left: Double,
    val right: Double,
    val operator: String,
) {
    fun evaluate(): Boolean {
        Log.i("ExpressionEvaluator", "Evaluating $left $operator $right")
        if (operator == "<") {
            return left < right
        } else if (operator == ">") {
            return left > right
        } else if (operator == "<=") {
            return left <= right
        } else if (operator == ">=") {
            return left >= right
        } else if (operator == "==") {
            return left == right
        } else if (operator == "!=") {
            return left != right
        }
        throw Exception("Invalid operator")
    }
}

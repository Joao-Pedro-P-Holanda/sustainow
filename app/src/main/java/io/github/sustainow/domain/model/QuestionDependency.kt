package io.github.sustainow.domain.model

data class QuestionDependency (
    val idRequiredQuestion:Int,
    val idDependantQuestion:Int,
    val dependencyExpression:String
)
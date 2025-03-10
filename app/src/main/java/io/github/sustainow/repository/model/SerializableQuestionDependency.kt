package io.github.sustainow.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SerializableQuestionDependency(
    @SerialName("id_required_question")
    val idRequiredQuestion: Int,
    @SerialName("dependency_expression")
    val dependencyExpression: String,
    @SerialName("id_dependant")
    val idDependantQuestion: Int,
)

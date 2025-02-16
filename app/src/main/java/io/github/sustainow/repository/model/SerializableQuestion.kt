package io.github.sustainow.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SerializableQuestion(
    val id: Int,
    val type: String,
    val name: String? = null,
    val text: String,
    @SerialName("question_alternative")
    val alternatives: List<SerializableQuestionAlternative>,
    @SerialName("dependent_question")
    val dependencies: List<SerializableQuestionDependency>,
    @SerialName("question_group")
    val questionGroup: SerializableGroupNameWrapper?,
)

@Serializable
data class SerializableGroupNameWrapper(
    val name: String,
)

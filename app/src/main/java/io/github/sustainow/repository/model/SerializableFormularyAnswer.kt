package io.github.sustainow.repository.model

import kotlinx.datetime.Instant
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
data class SerializableFormularyAnswer(
    val id: Int,
    @SerialName("form_id")
    val formId: Int,
    @SerialName("user_id")
    val uid: String,
    val value: Float,
    @SerialName("time_period")
    var timePeriod: Duration?,
    val unit: String,
    @SerialName("group_name")
    val groupName: String? = null,
    @SerialName("question_id")
    val questionId: Int,
    @SerialName("answer_date")
    val answerDate: Instant,
    val type: String,
)

@Serializable
data class SerializableFormularyAnswerCreate(
    @SerialName("form_id")
    val formId: Int? = null,
    @SerialName("user_id")
    val uid: String,
    val text: String? = null,
    val value: Float,
    @SerialName("time_period")
    var timePeriod: Duration?,
    val unit: String,
    @SerialName("group_name")
    val groupName: String? = null,
    @SerialName("question_id")
    val questionId: Int,
)

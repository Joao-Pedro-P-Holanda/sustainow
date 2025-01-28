package io.github.sustainow.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SerializableActionMember (
    @SerialName("action_id")
    val actionId: Int,
    @SerialName("user_id")
    val userId: String,
)
package io.github.sustainow.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SerializableInvitation (
    val id:Int?,
    @SerialName("user_name")
    val invitedUser: SerializableUserProfile,
    @SerialName("action")
    val action: SerializableCollectiveActionBaseInfo,
    val accepted: Boolean?
)

@Serializable
data class SerializableCollectiveActionBaseInfo(
    val id: Int,
    val name: String
)
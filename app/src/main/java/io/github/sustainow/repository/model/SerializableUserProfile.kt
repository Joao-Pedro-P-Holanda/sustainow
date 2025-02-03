package io.github.sustainow.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
@SerialName("user_name")
data class SerializableUserProfile(
    @SerialName("id")
    val id: String,
    @SerialName("full_name")
    val name: String,
    @SerialName("profile_picture")
    val profilePicture: String? = null
)


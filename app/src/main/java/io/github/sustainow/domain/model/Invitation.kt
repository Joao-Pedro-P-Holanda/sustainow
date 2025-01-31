package io.github.sustainow.domain.model

data class Invitation(
    val id: Int?,
    val actionId: Int,
    val actionName: String,
    val invitedUser: UserProfile,
    val accepted: Boolean? = null
)
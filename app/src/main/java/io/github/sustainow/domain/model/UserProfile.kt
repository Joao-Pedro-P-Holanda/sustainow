package io.github.sustainow.domain.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class UserProfile @OptIn(ExperimentalUuidApi::class) constructor(
    val id:Uuid,
    val fullName: String,
    val profilePicture: String? = null
)
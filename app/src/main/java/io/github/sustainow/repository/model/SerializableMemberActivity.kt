package io.github.sustainow.repository.model

import io.github.sustainow.domain.model.ActivityType
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SerializableMemberActivity (
    val id:Int,
    @SerialName("action_id")
    val actionId:Int,
    @SerialName("user_name")
    val member: SerializableUserProfile,
    @SerialName("activity_type")
    val activityType: String,
    val comment: String?,
    @SerialName("created_at")
    val date: Instant
)

@Serializable
data class SerializableMemberActivityCreate(
    @SerialName("action_id")
    val actionId:Int,
    @SerialName("member_id")
    val memberId:String,
    @SerialName("activity_type")
    val activityType: String,
    val comment: String? = null
)

package io.github.sustainow.repository.model

import io.github.sustainow.domain.model.ActivityType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SerializableMemberActivity (
    val id:Int?=null,
    @SerialName("action_id")
    val actionId:Int,
    val member: SerializableUserProfile,
    @SerialName("activity_type")
    val activityType: ActivityType
)

@Serializable
data class SerializableMemberActivityCreate(
    @SerialName("action_id")
    val actionId:Int,
    @SerialName("member_id")
    val memberId:String,
    @SerialName("activity_type")
    val activityType: String
)

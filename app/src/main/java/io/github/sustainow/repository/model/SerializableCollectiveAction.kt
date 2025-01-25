package io.github.sustainow.repository.model

import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SerializableCollectiveAction (
    val id: Int?,
    var images: List<String>?=null,
    val name: String,
    val description: String,
    val metadata: SerializableUserProfile,
    @SerialName("start_date")
    val startDate: LocalDate,
    @SerialName("end_date")
    val endDate: LocalDate,
    val status: String,
    @SerialName("action_member")
    val members: List<SerializableUserProfile>
)


@Serializable
data class SerializableCollectiveActionCreate(
   val name: String,
   val description: String,
   val status: String,
   @SerialName("start_date")
   val startDate: LocalDate,
   @SerialName("end_date")
   val endDate: LocalDate,
)

@Serializable
data class SerializableCollectiveActionUpdate(
    val id: Int,
    val name: String,
    val description: String,
    val status: String,
    @SerialName("start_date")
    val startDate: LocalDate,
    @SerialName("end_date")
    val endDate: LocalDate,
)


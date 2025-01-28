package io.github.sustainow.domain.model

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import java.time.OffsetDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class MemberActivity (
    val id: Int? = null,
    val memberProfile: UserProfile,
    val actionId: Int,
    val type: ActivityType,
    val comment: String? = null,
    val date: LocalDateTime
)

data class MemberActivityCreate (
    val memberId: String,
    val actionId: Int,
    val type: ActivityType,
    val comment: String? = null
)

enum class ActivityType(val type: String) {
    COMMENT("comment"),
    JOIN("join"),
    CHANGE_STATUS("change_status"),
}

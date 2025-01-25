package io.github.sustainow.domain.model

import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class MemberActivity @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Int? =null,
    val authorId: Uuid,
    val authorName: String,
    val actionId: Int,
    val type: ActivityType
)

enum class ActivityType(val type: String) {
    COMMENT("comment"),
    JOIN("join"),
    CHANGE_STATUS("change_status"),
}

package io.github.sustainow.domain.model

import android.net.Uri
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class CollectiveAction @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Int?,
    var images:List<Uri>,
    val name: String,
    val description:String,
    val authorId: Uuid,
    val authorName:String?,
    val startDate:LocalDate,
    val endDate:LocalDate,
    val status:String,
    val members: List<UserProfile>
)
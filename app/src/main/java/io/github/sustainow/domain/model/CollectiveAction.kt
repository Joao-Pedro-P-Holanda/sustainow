package io.github.sustainow.domain.model

import android.net.Uri
import kotlinx.datetime.LocalDate
import kotlin.uuid.Uuid

data class CollectiveAction(
    val id: Int?,
    var images:List<Uri>,
    val name: String,
    val description:String,
    // TODO use uuid
    val authorId: String,
    val authorName:String?,
    val startDate:LocalDate,
    val endDate:LocalDate,
    val status:String,
)
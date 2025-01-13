package io.github.sustainow.repository.model

import android.net.Uri
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SerializableCollectiveAction (
    val id: Int?,
    val images: List<String>,
    val name: String,
    val description: String,
    @SerialName("full_name")
    val authorName: String,
    @SerialName("start_date")
    val startDate: LocalDate,
    @SerialName("end_date")
    val endDate: LocalDate,
    val status: String,
)
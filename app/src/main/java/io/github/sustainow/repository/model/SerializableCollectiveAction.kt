package io.github.sustainow.repository.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SerializableCollectiveAction (
    val id: Int ,
    val images: List<String> ,
    val name: String ,
    val description: String ,
    @SerialName("full_name")
    val authorName: String ,
    @SerialName("start_date")
    val startDate: String ,

    val endDate: String ,
    val status: String ,
)
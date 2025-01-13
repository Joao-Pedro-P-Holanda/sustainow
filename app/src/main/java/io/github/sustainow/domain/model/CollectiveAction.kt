package io.github.sustainow.domain.model

import android.graphics.Bitmap
import android.net.Uri
import kotlinx.datetime.LocalDate

data class CollectiveAction(
    val id: Int?,
    val images:List<Uri>,
    val name: String,
    val description:String,
    val author:String,
    val startDate:LocalDate,
    val endDate:LocalDate,
    val status:String,
)
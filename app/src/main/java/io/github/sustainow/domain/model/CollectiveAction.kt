package io.github.sustainow.domain.model

import android.graphics.Bitmap
import kotlinx.datetime.LocalDate

data class CollectiveAction(
    val id: Int?,
    val images:List<Bitmap>,
    val name: String,
    val description:String,
    val author:String,
    val startDate:LocalDate,
    val endDate:LocalDate,
    val status:String,
)
package io.github.sustainow.domain.model

import android.graphics.Bitmap

data class LabeledImage (
    val image: Bitmap,
    val label: String,
    val supportingText: String,
)
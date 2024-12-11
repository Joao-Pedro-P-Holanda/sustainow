package io.github.sustainow.domain.model

import android.graphics.Bitmap

data class LabeledImage(
    val image: Bitmap? = null,
    val videoUrl: String? = null, // URL ou caminho do vídeo
    val label: String,
    val supportingText: String
)
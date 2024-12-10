package io.github.sustainow.domain.model

data class LabeledImage (
    val image: Int,
    val label: String,
    val supportingText: String,
)
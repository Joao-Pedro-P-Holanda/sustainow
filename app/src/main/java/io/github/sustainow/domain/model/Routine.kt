package io.github.sustainow.domain.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Routine @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Int,
    val userId: Uuid,
    val listOfTasks: List<Task>
)

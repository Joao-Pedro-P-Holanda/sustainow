package io.github.sustainow.domain.model

import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi

data class Routine @OptIn(ExperimentalUuidApi::class) constructor(
    val id: Int,
    val userId: UUID,
    val listOfTasks: List<Task>
)

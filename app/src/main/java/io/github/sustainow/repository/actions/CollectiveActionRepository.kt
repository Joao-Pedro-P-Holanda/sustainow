package io.github.sustainow.repository.actions

import io.github.sustainow.domain.model.CollectiveAction

interface CollectiveActionRepository {
    suspend fun list(): List<CollectiveAction>
    suspend fun get(id: Int): CollectiveAction
    suspend fun create(collectiveAction: CollectiveAction): CollectiveAction
    suspend fun update(collectiveAction: CollectiveAction): CollectiveAction
    suspend fun delete(id: Int)
}
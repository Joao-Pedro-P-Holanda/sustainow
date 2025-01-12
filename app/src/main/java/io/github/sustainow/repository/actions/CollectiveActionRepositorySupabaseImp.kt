package io.github.sustainow.repository.actions

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.sustainow.domain.model.CollectiveAction
import io.github.sustainow.exceptions.ResponseException
import io.github.sustainow.exceptions.TimeoutException
import io.github.sustainow.exceptions.UnknownException
import io.github.sustainow.repository.mapper.SupabaseMapper
import io.github.sustainow.repository.model.SerializableCollectiveAction
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.datetime.LocalDate
import javax.inject.Inject

class CollectiveActionRepositorySupabaseImp @Inject constructor(
    private val supabase: SupabaseClient,
    private val tableName: String
) : CollectiveActionRepository {
    private val mapper = SupabaseMapper()

    override suspend fun list(): List<CollectiveAction> {
        try {
            val response = supabase.from(tableName).select().decodeAs<List<SerializableCollectiveAction>>()
            return response.map { mapper.toDomain(it) }
        } catch (e: RestException) {
            throw ResponseException("Error listing collective actions", e)
        } catch (e: HttpRequestException) {
            throw UnknownException("Server error", e)
        } catch (e: HttpRequestTimeoutException) {
            throw TimeoutException("Timeout exception", e)
        }
    }

    override suspend fun get(id: Int): CollectiveAction {
        try {
            val response = supabase.from(tableName).select().eq("id", id).decodeSingle<SerializableCollectiveAction>()
            return mapper.toDomain(response)
        } catch (e: RestException) {
            throw ResponseException("Error getting collective action", e)
        } catch (e: HttpRequestException) {
            throw UnknownException("Server error", e)
        } catch (e: HttpRequestTimeoutException) {
            throw TimeoutException("Timeout exception", e)
        }
    }

    override suspend fun create(collectiveAction: CollectiveAction): CollectiveAction {
        try {
            val response = supabase.from(tableName).insert(mapper.toSerializable(collectiveAction)).decodeSingle<SerializableCollectiveAction>()
            return mapper.toDomain(response)
        } catch (e: RestException) {
            throw ResponseException("Error creating collective action", e)
        } catch (e: HttpRequestException) {
            throw UnknownException("Server error", e)
        } catch (e: HttpRequestTimeoutException) {
            throw TimeoutException("Timeout exception", e)
        }
    }

    override suspend fun update(collectiveAction: CollectiveAction): CollectiveAction {
        try {
            val response = supabase.from(tableName).update(mapper.toSerializable(collectiveAction)).eq("id", collectiveAction.id).decodeSingle<SerializableCollectiveAction>()
            return mapper.toDomain(response)
        } catch (e: RestException) {
            throw ResponseException("Error updating collective action", e)
        } catch (e: HttpRequestException) {
            throw UnknownException("Server error", e)
        } catch (e: HttpRequestTimeoutException) {
            throw TimeoutException("Timeout exception", e)
        }
    }

    override suspend fun delete(id: Int) {
        try {
            supabase.from(tableName).delete().eq("id", id)
        } catch (e: RestException) {
            throw ResponseException("Error deleting collective action", e)
        } catch (e: HttpRequestException) {
            throw UnknownException("Server error", e)
        } catch (e: HttpRequestTimeoutException) {
            throw TimeoutException("Timeout exception", e)
        }
    }
}
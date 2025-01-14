package io.github.sustainow.repository.actions

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.storage.Bucket
import io.github.jan.supabase.storage.BucketApi
import io.github.jan.supabase.storage.storage
import io.github.sustainow.domain.model.CollectiveAction
import io.github.sustainow.exceptions.ResponseException
import io.github.sustainow.exceptions.TimeoutException
import io.github.sustainow.exceptions.UnknownException
import io.github.sustainow.presentation.ui.utils.ByteArrayWithMime
import io.github.sustainow.repository.mapper.SupabaseMapper
import io.github.sustainow.repository.model.SerializableCollectiveAction
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.LocalDate
import kotlinx.io.IOException
import java.time.Duration
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CollectiveActionRepositorySupabaseImp @Inject constructor(
    private val supabase: SupabaseClient,
    private val tableName: String,
    private val context: Context
) : CollectiveActionRepository {
    private val mapper = SupabaseMapper()

    override suspend fun list(): List<CollectiveAction> {
        try {
            val response = supabase.from(tableName).select(
                Columns.raw(
                    """
                        id,
                        name,
                        start_date,
                        end_date,
                        description,
                        status,
                        user_name(id,full_name)
                    """
                )
            ).decodeAs<List<SerializableCollectiveAction>>()

            response.map {
                try {
                    val bucket = resolveBucketForAction(it)
                    it.images = getImagesFromBucket(bucket).map {
                        it.toString()
                    }
                }
                catch(e: Exception){
                    Log.e("CollectiveActionRepositorySupabaseImp", "Error getting images from bucket", e)
                }
            }

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
            val response = supabase.from(tableName).select(
                Columns.raw(
                """
                        id,
                        name,
                        start_date,
                        end_date,
                        description,
                        status,
                        user_name(id,full_name)
                    """
                )
            ){
                filter{eq("id", id)}
            }.decodeSingle<SerializableCollectiveAction>()

            val result = mapper.toDomain(response)
            try {
                result.images = getImagesFromBucket(resolveBucketForAction(result))
            }
            catch(e: Exception){
                Log.e("CollectiveActionRepositorySupabaseImp", "Error getting images from bucket", e)
            }
            return result

        } catch (e: RestException) {
            throw ResponseException("Error getting collective action", e)
        } catch (e: HttpRequestException) {
            throw UnknownException("Server error", e)
        } catch (e: HttpRequestTimeoutException) {
            throw TimeoutException("Timeout exception", e)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun create(collectiveAction: CollectiveAction): CollectiveAction {
        try {
            val response = supabase.from(tableName).insert(
                mapper.toSerializableCreate(collectiveAction)){
                select(
                    Columns.raw(
                    """
                        id,
                        name,
                        start_date,
                        end_date,
                        description,
                        status,
                        user_name(id,full_name)
                    """
                ))
            }
            .decodeSingle<SerializableCollectiveAction>()
            supabase.storage.createBucket(
                "action-${response.id}-images") {
                allowedMimeTypes("image/*")
                public = true
            }
            val bucket = supabase.storage["action-${response.id}-images"]
            // TODO replace simple uri for a data object with the image name
            for (image in collectiveAction.images) {
                val withType = generateBytesFromUri(image).getOrThrow()
                bucket.upload(Uuid.random().toString(), withType.byteArray){
                    contentType = ContentType(withType.mimeType,withType.mimeSubType)
                }
            }

            val result = mapper.toDomain(response)
            try {
                result.images = getImagesFromBucket(bucket)
            } catch (e: Exception) {
                Log.e("CollectiveActionRepositorySupabaseImp", "Error getting images from bucket", e)
            }

            return result
        }
        catch(e: IOException){
            throw UnknownException("Error uploading image", e)
        }
        catch (e: RestException) {
            throw ResponseException("Error creating collective action", e)
        } catch (e: HttpRequestException) {
            throw UnknownException("Server error", e)
        } catch (e: HttpRequestTimeoutException) {
            throw TimeoutException("Timeout exception", e)
        }
    }

    override suspend fun update(collectiveAction: CollectiveAction): CollectiveAction {
        try {
            val response = supabase.from(tableName).update(mapper.toSerializableUpdate(collectiveAction)){
                select(
                    Columns.raw(
                    """
                        id,
                        name,
                        start_date,
                        end_date,
                        description,
                        status,
                        user_name(id,full_name)
                    """
                ))
            }.decodeSingle<SerializableCollectiveAction>()
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
            supabase.from(tableName).delete{
                filter{
                    eq("id",id)
                }
            }
        } catch (e: RestException) {
            throw ResponseException("Error deleting collective action", e)
        } catch (e: HttpRequestException) {
            throw UnknownException("Server error", e)
        } catch (e: HttpRequestTimeoutException) {
            throw TimeoutException("Timeout exception", e)
        }
    }

    private suspend fun getImagesFromBucket(bucket: BucketApi): List<Uri>{
        val fileNames = bucket.list().map {
            it.name
        }
        print(fileNames)
        return bucket.createSignedUrls(60.minutes,*fileNames.toTypedArray()).map{
            Uri.parse(it.signedURL)
        }
    }
    private fun resolveBucketForAction(action: CollectiveAction): BucketApi {
        return supabase.storage["action-${action.id}-images"]
    }
    private fun resolveBucketForAction(action: SerializableCollectiveAction): BucketApi {
        return supabase.storage["action-${action.id}-images"]

    }

    private suspend fun generateBytesFromUri(uri: Uri): Result<ByteArrayWithMime> = withContext(Dispatchers.IO){
        try {
            val mimeType = context.contentResolver.getType(uri)
            if (mimeType?.startsWith("image/") != true) {
                return@withContext Result.failure(IllegalArgumentException("URI does not point to an image"))
            }

            val parts = mimeType.split("/")

            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                Result.success(
                    ByteArrayWithMime(byteArray = inputStream.readBytes(),mimeType = parts[0],
                        mimeSubType = parts[1]
                    ))
            } ?: Result.failure(IOException("Could not open input stream for URI: $uri"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
package io.github.sustainow.repository.actions

import android.content.Context
import android.net.Uri
import android.util.Log
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.BucketApi
import io.github.jan.supabase.storage.storage
import io.github.sustainow.domain.model.ActivityType
import io.github.sustainow.domain.model.MemberActivity
import io.github.sustainow.domain.model.CollectiveAction
import io.github.sustainow.domain.model.Invitation
import io.github.sustainow.domain.model.MemberActivityCreate
import io.github.sustainow.domain.model.UserProfile
import io.github.sustainow.exceptions.ResponseException
import io.github.sustainow.exceptions.TimeoutException
import io.github.sustainow.exceptions.UnknownException
import io.github.sustainow.presentation.ui.utils.ByteArrayWithMime
import io.github.sustainow.repository.mapper.SupabaseMapper
import io.github.sustainow.repository.model.SerializableActionMember
import io.github.sustainow.repository.model.SerializableCollectiveAction
import io.github.sustainow.repository.model.SerializableInvitation
import io.github.sustainow.repository.model.SerializableInvitationCreate
import io.github.sustainow.repository.model.SerializableMemberActivity
import io.github.sustainow.repository.model.SerializableMemberActivityCreate
import io.github.sustainow.repository.model.SerializableUserProfile
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.http.ContentType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.io.IOException
import kotlinx.serialization.ExperimentalSerializationApi
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CollectiveActionRepositorySupabaseImp @Inject constructor(
    private val supabase: SupabaseClient,
    private val actionTableName: String,
    private val invitationTableName: String,
    private val memberTableName: String,
    private val memberActivityTableName: String,
    private val usernameTableName: String,
    private val context: Context
) : CollectiveActionRepository {
    private val mapper = SupabaseMapper()

    override suspend fun list(): List<CollectiveAction> {
        try {
            val response = supabase.from(actionTableName).select(
                Columns.raw(
                    """
                        id,
                        name,
                        start_date,
                        end_date,
                        description,
                        status,
                        user_name!action_author_id_fkey1(id,full_name),
                        ${memberTableName}(user_name(id,full_name))
                    """.trimIndent()
                )
            )
            Log.i("CollectiveActionRepositorySupabaseImp", response.data)

            val decoded = response.decodeAs<List<SerializableCollectiveAction>>()

            decoded.map {
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

            return decoded.map { mapper.toDomain(it) }
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
            val response = supabase.from(actionTableName).select(
                Columns.raw(
                """
                        id,
                        name,
                        start_date,
                        end_date,
                        description,
                        status,
                        user_name!action_author_id_fkey1(id,full_name),
                        ${memberTableName}(user_name(id,full_name))
                    """.trimIndent()
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
            val response = supabase.from(actionTableName).insert(
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
                        user_name!action_author_id_fkey1(id,full_name),
                        ${memberTableName}(user_name(id,full_name))
                    """.trimIndent()
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

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun update(collectiveAction: CollectiveAction): CollectiveAction {
        Log.i("UpdateCollectiveActions",collectiveAction.images.toString())
        if (collectiveAction.id == null) {
            throw IllegalArgumentException("CollectiveAction id cannot be null or blank")
        }
        try {
            val response = supabase.from(actionTableName).update(mapper.toSerializableUpdate(collectiveAction)){
                filter{
                    eq("id",collectiveAction.id)
                }
                select(
                    Columns.raw(
                        """
                    id,
                        name,
                        start_date,
                        end_date,
                        description,
                        status,
                        user_name!action_author_id_fkey1(id,full_name),
                        ${memberTableName}(user_name(id,full_name))
                    """.trimIndent()
                ))
            }.decodeSingle<SerializableCollectiveAction>()
            // replace/remove images only if the existing is not an remote url
            if(collectiveAction.images.all{
                it.toString().startsWith("content://")
            }){
                val bucket = supabase.storage["action-${response.id}-images"]
                removeImagesFromBucket(bucket)
                // TODO replace simple uri for a data object with the image name
                for (image in collectiveAction.images) {
                    val withType = generateBytesFromUri(image).getOrThrow()
                    bucket.upload(Uuid.random().toString(), withType.byteArray) {
                        contentType = ContentType(withType.mimeType, withType.mimeSubType)
                    }
                }
            }
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
            supabase.from(actionTableName).delete{
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

    override suspend fun listActionActivities(actionId: Int): List<MemberActivity> {
       try {
              val response = supabase.from(memberActivityTableName).select(
                Columns.raw(
                     """
                            id,
                            ${usernameTableName}(id,full_name),
                            action_id,
                            activity_type,
                            comment,
                            created_at
                      """.trimIndent()
                )
              ){
                filter{
                     eq("action_id",actionId)
                }
              }
              Log.i("MemberActivity",response.data)

              val result = response.decodeList<SerializableMemberActivity>().map {
                mapper.toDomain(it)
              }

              return result
         }
         catch (e: RestException) {
              throw ResponseException("Error listing member activities", e)
         } catch (e: HttpRequestException) {
              throw UnknownException("Server error", e)
         } catch (e: HttpRequestTimeoutException) {
              throw TimeoutException("Timeout exception", e)
       }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun listPendingInvitations(userId: Uuid): List<Invitation> {
        try{
            val response=  supabase.from(invitationTableName).select(
                Columns.raw(
                    """
                        ${usernameTableName}(id,full_name),
                        id,
                        ${actionTableName}(id,name),
                        accepted
                    """.trimIndent()
                )
            ){
                filter {
                   exact("accepted",null)
                    eq("invited_id",userId.toString())
                }
            }
            Log.i("Invitations",response.data)

            val result = response.decodeList<SerializableInvitation>().map {
                mapper.toDomain(it)
            }

            return result
        }
        catch (e: RestException) {
            throw ResponseException("Error listing invitations", e)
        } catch (e: HttpRequestException) {
            throw UnknownException("Server error", e)
        } catch (e: HttpRequestTimeoutException) {
            throw TimeoutException("Timeout exception", e)
        }
    }

    override suspend fun listActionInvitations(actionId: Int): List<Invitation> {
        try{
            val response=  supabase.from(invitationTableName).select(
                Columns.raw(
                    """
                        ${usernameTableName}(id,full_name),
                        id,
                        ${actionTableName}(id,name),
                        accepted
                    """.trimIndent()
                )
            ){
                filter {
                    exact("accepted",null)
                    exact("accepted",false)
                    eq("action_id",actionId)
                }
            }
            Log.i("Invitations",response.data)

            val result = response.decodeList<SerializableInvitation>().map {
                mapper.toDomain(it)
            }

            return result
        }
        catch (e: RestException) {
            throw ResponseException("Error listing invitations", e)
        } catch (e: HttpRequestException) {
            throw UnknownException("Server error", e)
        } catch (e: HttpRequestTimeoutException) {
            throw TimeoutException("Timeout exception", e)
        }
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun inviteMembers(id: Int, userProfiles: Iterable<UserProfile>)  {
        try{
            supabase.from(invitationTableName).insert(
                userProfiles.map{ SerializableInvitationCreate(
                    actionId = id,
                    userId = it.id.toString()
                )}
            )
        }
        catch (e: RestException) {
            throw ResponseException("Error inviting members", e)
        } catch (e: HttpRequestException) {
            throw UnknownException("Server error", e)
        } catch (e: HttpRequestTimeoutException) {
            throw TimeoutException("Timeout exception", e)
        }
    }

    /**
     * Changes the accepted status of a invitation
     * if the invitation is accepted, a member activity is created
     */
    @OptIn(ExperimentalUuidApi::class)
    override suspend fun answerInvitation(invitation: Invitation) {
        if (invitation.id == null) {
            throw IllegalArgumentException("Invitation id cannot be null or blank")
        }
        try{
            val currentInstant = Clock.System.now()
            val currentDate = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault()).date
            supabase.from(invitationTableName).update(
                {
                    set("accepted", invitation.accepted)
                    set("response_date", currentDate)
                }
            ){
                filter { eq("id", invitation.id) }
            }
            if(invitation.accepted == true) {
                supabase.from(memberTableName).insert(
                   SerializableActionMember(
                        actionId = invitation.actionId,
                        userId = invitation.invitedUser.id.toString()
                   )
                )
                supabase.from(memberActivityTableName).insert(
                    SerializableMemberActivityCreate(
                        memberId = invitation.invitedUser.id.toString(),
                        actionId = invitation.actionId,
                        activityType = ActivityType.JOIN.type
                    )
                )
            }
        }
        catch (e: RestException) {
            throw ResponseException("Error answering invitation", e)
        } catch (e: HttpRequestException) {
            throw UnknownException("Server error", e)
        } catch (e: HttpRequestTimeoutException) {
            throw TimeoutException("Timeout exception", e)
        }
    }

    override suspend fun addComment(comment: MemberActivityCreate) {
        try{
            if(comment.type != ActivityType.COMMENT){
                throw IllegalArgumentException("MemberActivity type must be COMMENT")
            }
            supabase.from(memberActivityTableName).insert(
               mapper.toSerializableCreate(
                  comment
               )
            )
        }
        catch (e: RestException) {
            throw ResponseException("Error adding comment", e)
        } catch (e: HttpRequestException) {
            throw UnknownException("Server error", e)
        } catch (e: HttpRequestTimeoutException) {
            throw TimeoutException("Timeout exception", e)
        }
    }

    override suspend fun removeComment(id: Int) {
        try{
            supabase.from(memberActivityTableName).delete{
                filter{
                    eq("id",id)
                }
            }
        }
        catch (e: RestException) {
            throw ResponseException("Error removing comment", e)
        } catch (e: HttpRequestException) {
            throw UnknownException("Server error", e)
        } catch (e: HttpRequestTimeoutException) {
            throw TimeoutException("Timeout exception", e)
        }
    }

    private suspend fun getImagesFromBucket(bucket: BucketApi): List<Uri>{
        Log.i("CollectiveActionRepositorySupabaseImp", bucket.list().toString())
        val fileNames = bucket.list().map {
            it.name
        }
        return bucket.createSignedUrls(60.minutes,*fileNames.toTypedArray()).map{
            Uri.parse(it.signedURL)
        }
    }
    private suspend fun removeImagesFromBucket(bucket: BucketApi) {
        try {
            bucket.delete(bucket.list().map {
                it.name
            })
        } catch (e: Exception) {
            Log.e("CollectiveActionRepositorySupabaseImp", "Error deleting images from bucket", e)
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
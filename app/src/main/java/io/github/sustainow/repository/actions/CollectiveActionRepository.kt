package io.github.sustainow.repository.actions

import io.github.sustainow.domain.model.MemberActivity
import io.github.sustainow.domain.model.CollectiveAction
import io.github.sustainow.domain.model.Invitation
import io.github.sustainow.domain.model.MemberActivityCreate
import io.github.sustainow.domain.model.UserProfile
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

interface CollectiveActionRepository {
    suspend fun list(): List<CollectiveAction>
    suspend fun get(id: Int): CollectiveAction
    suspend fun create(collectiveAction: CollectiveAction): CollectiveAction
    suspend fun update(collectiveAction: CollectiveAction): CollectiveAction
    suspend fun delete(id: Int)
    suspend fun listActionActivities(actionId: Int): List<MemberActivity>
    @OptIn(ExperimentalUuidApi::class)
    suspend fun listPendingInvitations(userId:Uuid): List<Invitation>
    suspend fun listActionInvitations(actionId: Int): List<Invitation>
    suspend fun inviteMembers(id: Int, emails: List<UserProfile>)
    suspend fun answerInvitation(invitation: Invitation)
    suspend fun addComment(comment: MemberActivityCreate)
    suspend fun removeComment(id: Int)
}
package io.github.sustainow.repository.mapper

import android.net.Uri
import io.github.sustainow.domain.model.ActivityType
import io.github.sustainow.domain.model.CollectiveAction
import io.github.sustainow.domain.model.Formulary
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.Invitation
import io.github.sustainow.domain.model.MemberActivity
import io.github.sustainow.domain.model.MemberActivityCreate
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.QuestionAlternative
import io.github.sustainow.domain.model.QuestionDependency
import io.github.sustainow.domain.model.UserProfile
import io.github.sustainow.repository.model.SerializableCollectiveAction
import io.github.sustainow.repository.model.SerializableCollectiveActionBaseInfo
import io.github.sustainow.repository.model.SerializableCollectiveActionCreate
import io.github.sustainow.repository.model.SerializableCollectiveActionUpdate
import io.github.sustainow.repository.model.SerializableFormulary
import io.github.sustainow.repository.model.SerializableFormularyAnswer
import io.github.sustainow.repository.model.SerializableInvitation
import io.github.sustainow.repository.model.SerializableMemberActivity
import io.github.sustainow.repository.model.SerializableMemberActivityCreate
import io.github.sustainow.repository.model.SerializableQuestion
import io.github.sustainow.repository.model.SerializableQuestionAlternative
import io.github.sustainow.repository.model.SerializableQuestionDependency
import io.github.sustainow.repository.model.SerializableUserProfile
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SupabaseMapper {
    fun toDomain(serialized: SerializableFormulary): Formulary {
        return Formulary(
            id = serialized.id,
            area = serialized.area,
            type = serialized.type,
            questions = serialized.questions.map { toDomain(it) },
        )
    }

    fun toSerializable(domain: Formulary): SerializableFormulary {
        return SerializableFormulary(
            id = domain.id,
            area = domain.area,
            type = domain.type,
            questions = domain.questions.map { toSerializable(it) },
        )
    }

    fun toDomain(serialized: SerializableQuestion): Question {
        return when (serialized.type) {
            "single-select" ->
                Question.SingleSelect(
                    id = serialized.id,
                    name = serialized.name,
                    text = serialized.text,
                    alternatives = serialized.alternatives.map { toDomain(it) },
                    dependencies = serialized.dependencies.map { toDomain(it) },
                )
            "multi-select" ->
                Question.MultiSelect(
                    id = serialized.id,
                    name = serialized.name,
                    text = serialized.text,
                    alternatives = serialized.alternatives.map { toDomain(it) },
                    dependencies = serialized.dependencies.map { toDomain(it) },
                )
            "numerical" ->
                Question.Numerical(
                    id = serialized.id,
                    name = serialized.name,
                    text = serialized.text,
                    alternatives = serialized.alternatives.map { toDomain(it) },
                    dependencies = serialized.dependencies.map { toDomain(it) },
                )
            "multi-group" ->
                Question.MultiItem(
                    id = serialized.id,
                    name = serialized.name,
                    text = serialized.text,
                    alternatives = serialized.alternatives.map { toDomain(it) }.toMutableList(),
                    dependencies = serialized.dependencies.map { toDomain(it) },
                )
            else -> {
                throw IllegalArgumentException("Invalid question type")
            }
        }
    }

    fun toSerializable(domain: Question): SerializableQuestion {
        if (domain.id == null) {
            throw IllegalArgumentException("Question id cannot be null or blank")
        }
        return when (domain) {
            is Question.SingleSelect ->
                SerializableQuestion(
                    id = domain.id!!,
                    name = domain.name,
                    text = domain.text,
                    type = "single-select",
                    alternatives = domain.alternatives.map { toSerializable(it) },
                    dependencies = domain.dependencies.map { toSerializable(it) },
                )
            is Question.MultiSelect ->
                SerializableQuestion(
                    id = domain.id!!,
                    name = domain.name,
                    text = domain.text,
                    type = "multi-select",
                    alternatives = domain.alternatives.map { toSerializable(it) },
                    dependencies = domain.dependencies.map { toSerializable(it) },
                )
            is Question.Numerical ->
                SerializableQuestion(
                    id = domain.id!!,
                    name = domain.name,
                    text = domain.text,
                    type = "numerical",
                    alternatives = domain.alternatives.map { toSerializable(it) },
                    dependencies = domain.dependencies.map { toSerializable(it) },
                )
            is Question.MultiItem ->
                SerializableQuestion(
                    id = domain.id!!,
                    name = domain.name,
                    text = domain.text,
                    type = "multi-group",
                    alternatives = domain.alternatives.map { toSerializable(it) },
                    dependencies = domain.dependencies.map { toSerializable(it) },
                )
        }
    }

    fun toDomain(serialized: SerializableQuestionAlternative): QuestionAlternative {
        return QuestionAlternative(
            area = serialized.area,
            name = serialized.name,
            text = serialized.text,
            value = serialized.value,
            timePeriod = serialized.timePeriod,
            unit = serialized.unit,
        )
    }

    fun toSerializable(domain: QuestionAlternative): SerializableQuestionAlternative {
        return SerializableQuestionAlternative(
            area = domain.area,
            name = domain.name,
            text = domain.text,
            value = domain.value,
            timePeriod = domain.timePeriod,
            unit = domain.unit,
        )
    }

    fun toDomain(serialized: SerializableFormularyAnswer): FormularyAnswer {
        return FormularyAnswer(
            id = serialized.id,
            formId = serialized.formId,
            uid = serialized.uid,
            groupName = serialized.groupName,
            questionId = serialized.questionId,
            answerDate = serialized.answerDate,
            value = serialized.value,
            timePeriod = serialized.timePeriod,
            month = serialized.month,
            unit = serialized.unit,
            type = serialized.type
        )
    }

    fun toSerializable(domain: FormularyAnswer): SerializableFormularyAnswer {
        if (domain.questionId == null) {
            throw IllegalArgumentException("FormularyAnswer id cannot be null or blank")
        }
        return SerializableFormularyAnswer(
            id = domain.id,
            formId = domain.formId,
            uid = domain.uid,
            value = domain.value,
            timePeriod = domain.timePeriod,
            unit = domain.unit,
            groupName = domain.groupName,
            questionId = domain.questionId,
            answerDate = domain.answerDate,
            month = domain.month,
            type = domain.type
        )
    }

    fun toDomain(serializable: SerializableQuestionDependency): QuestionDependency {
        return QuestionDependency(idDependantQuestion = serializable.idDependantQuestion, idRequiredQuestion = serializable.idRequiredQuestion , dependencyExpression = serializable.dependencyExpression)
    }

    fun toSerializable(domain: QuestionDependency): SerializableQuestionDependency {
        return SerializableQuestionDependency(
            idDependantQuestion = domain.idDependantQuestion,
            idRequiredQuestion = domain.idRequiredQuestion,
            dependencyExpression = domain.dependencyExpression,
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    fun toDomain(serialized: SerializableCollectiveAction): CollectiveAction {
        return CollectiveAction(
            id = serialized.id,
                images = serialized.images?.map {
                uri -> Uri.parse(uri)
            } ?: emptyList(),
            name = serialized.name,
            description = serialized.description,
            authorId = Uuid.parse(serialized.metadata.id),
            authorName = serialized.metadata.name,
            startDate = serialized.startDate,
            endDate = serialized.endDate,
            status = serialized.status,
            members = serialized.members.map { toDomain(it.profile) }
        )
    }

    fun toSerializableCreate(domain: CollectiveAction): SerializableCollectiveActionCreate {
        return SerializableCollectiveActionCreate(
            name = domain.name,
            description = domain.description,
            startDate = domain.startDate,
            endDate = domain.endDate,
            status = domain.status
        )
    }
    fun toSerializableUpdate(domain: CollectiveAction): SerializableCollectiveActionUpdate{
        if(domain.id== null){
            throw IllegalArgumentException("CollectiveAction id cannot be null or blank")
        }
        return SerializableCollectiveActionUpdate(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            startDate = domain.startDate,
            endDate = domain.endDate,
            status = domain.status
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    fun toDomain(serialized: SerializableInvitation): Invitation {
        return Invitation(
            id = serialized.id,
            invitedUser = UserProfile(Uuid.parse(serialized.invitedUser.id),serialized.invitedUser.name),
            actionId = serialized.action.id,
            actionName = serialized.action.name,
            accepted = serialized.accepted
        )
    }
    @OptIn(ExperimentalUuidApi::class)
    fun toSerializable(domain:Invitation):SerializableInvitation {
        return SerializableInvitation(
            id = domain.id,
            invitedUser = SerializableUserProfile(id = domain.invitedUser.id.toString(),name = domain.invitedUser.fullName),
            action = SerializableCollectiveActionBaseInfo(id = domain.actionId,name = domain.actionName),
            accepted = domain.accepted
        )
    }

    fun toDomain(serialized: SerializableMemberActivity): MemberActivity{
        val convertedEnum = enumValues<ActivityType>().find { it.type == serialized.activityType}
        if(convertedEnum == null){
            throw IllegalArgumentException("Invalid ActivityType")
        }
        return MemberActivity(
            id=serialized.id,
            memberProfile = toDomain(serialized.member),
            actionId = serialized.actionId,
            type = convertedEnum,
            comment = serialized.comment,
            date= serialized.date.toLocalDateTime(TimeZone.currentSystemDefault())
        )
    }

    fun toSerializableCreate(domain: MemberActivityCreate): SerializableMemberActivityCreate{
        return SerializableMemberActivityCreate(
            actionId = domain.actionId,
            memberId = domain.memberId,
            activityType = domain.type.type,
            comment = domain.comment,
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    fun toDomain(serialized: SerializableUserProfile): UserProfile {
        return UserProfile(
            id = Uuid.parse(serialized.id),
            fullName = serialized.name,
            profilePicture = serialized.profilePicture
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    fun toSerializable(domain: UserProfile): SerializableUserProfile {
        return SerializableUserProfile(
            id = domain.id.toString(),
            name = domain.fullName,
            profilePicture = domain.profilePicture
        )
    }


}

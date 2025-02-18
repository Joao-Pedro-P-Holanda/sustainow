package io.github.sustainow.repository.mapper

import android.net.Uri
import io.github.sustainow.domain.model.ActivityType
import io.github.sustainow.domain.model.CollectiveAction
import io.github.sustainow.domain.model.Formulary
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.FormularyAnswerCreate
import io.github.sustainow.domain.model.Invitation
import io.github.sustainow.domain.model.MemberActivity
import io.github.sustainow.domain.model.MemberActivityCreate
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.QuestionDependency
import io.github.sustainow.domain.model.Routine
import io.github.sustainow.domain.model.RoutineTask
import io.github.sustainow.domain.model.RoutineTaskMetaData
import io.github.sustainow.domain.model.UserProfile
import io.github.sustainow.repository.model.SerializableCollectiveAction
import io.github.sustainow.repository.model.SerializableCollectiveActionCreate
import io.github.sustainow.repository.model.SerializableCollectiveActionUpdate
import io.github.sustainow.repository.model.SerializableFormulary
import io.github.sustainow.repository.model.SerializableFormularyAnswer
import io.github.sustainow.repository.model.SerializableFormularyAnswerCreate
import io.github.sustainow.repository.model.SerializableInvitation
import io.github.sustainow.repository.model.SerializableMemberActivity
import io.github.sustainow.repository.model.SerializableMemberActivityCreate
import io.github.sustainow.repository.model.SerializableQuestion
import io.github.sustainow.repository.model.SerializableQuestionAlternative
import io.github.sustainow.repository.model.SerializableQuestionDependency
import io.github.sustainow.repository.model.SerializableRoutine
import io.github.sustainow.repository.model.SerializableRoutineTask
import io.github.sustainow.repository.model.SerializableRoutineTaskMetaData
import io.github.sustainow.repository.model.SerializableUserProfile
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class SupabaseMapper {
    fun toDomain(
        serialized: SerializableFormulary,
        userId: String,
    ): Formulary =
        Formulary(
            id = serialized.id,
            area = serialized.area,
            type = serialized.type,
            questions = serialized.questions.map { toDomain(it, userId = userId, formId = serialized.id) },
        )

    fun toDomain(
        serialized: SerializableQuestion,
        userId: String,
        formId: Int,
    ): Question =
        when (serialized.type) {
            "single-select" ->
                Question.SingleSelect(
                    id = serialized.id,
                    name = serialized.name,
                    text = serialized.text,
                    alternatives = serialized.alternatives.map { toDomain(it, userId = userId, formId = formId) },
                    dependencies = serialized.dependencies.map { toDomain(it) },
                )
            "multi-select" ->
                Question.MultiSelect(
                    id = serialized.id,
                    name = serialized.name,
                    text = serialized.text,
                    alternatives = serialized.alternatives.map { toDomain(it, userId = userId, formId = formId) },
                    dependencies = serialized.dependencies.map { toDomain(it) },
                )
            "numerical" ->
                Question.Numerical(
                    id = serialized.id,
                    name = serialized.name,
                    text = serialized.text,
                    alternatives = serialized.alternatives.map { toDomain(it, userId = userId, formId = formId) },
                    dependencies = serialized.dependencies.map { toDomain(it) },
                )
            "multi-group" ->
                Question.MultiItem(
                    id = serialized.id,
                    name = serialized.name,
                    text = serialized.text,
                    alternatives = serialized.alternatives.map { toDomain(it, userId, formId) }.toMutableList(),
                    dependencies = serialized.dependencies.map { toDomain(it) },
                )
            else -> {
                throw IllegalArgumentException("Invalid question type")
            }
        }

    fun toDomain(
        serialized: SerializableQuestionAlternative,
        userId: String,
        formId: Int,
    ): FormularyAnswerCreate =
        FormularyAnswerCreate(
            text = serialized.text,
            questionId = serialized.questionId,
            value = serialized.value,
            timePeriod = serialized.timePeriod,
            unit = serialized.unit,
            uid = userId,
            formId = formId,
            groupName = null,
        )

    fun toDomain(serialized: SerializableFormularyAnswer): FormularyAnswer =
        FormularyAnswer(
            id = serialized.id,
            formId = serialized.formId,
            uid = serialized.uid,
            groupName = serialized.groupName,
            questionId = serialized.questionId,
            answerDate = serialized.answerDate,
            value = serialized.value,
            timePeriod = serialized.timePeriod,
            unit = serialized.unit,
            type = serialized.type,
        )

    fun toDomainCreate(serialized: SerializableFormularyAnswer): FormularyAnswerCreate =
        FormularyAnswerCreate(
            value = serialized.value,
            timePeriod = serialized.timePeriod,
            unit = serialized.unit,
            uid = serialized.uid,
            formId = serialized.formId,
            groupName = serialized.groupName,
            questionId = serialized.questionId,
        )

    fun toSerializable(domain: FormularyAnswer): SerializableFormularyAnswer =
        SerializableFormularyAnswer(
            id = domain.id,
            formId = domain.formId,
            uid = domain.uid,
            value = domain.value,
            timePeriod = domain.timePeriod,
            unit = domain.unit,
            groupName = domain.groupName,
            questionId = domain.questionId,
            answerDate = domain.answerDate,
            type = domain.type,
        )

    fun toSerializableCreate(domain: FormularyAnswerCreate): SerializableFormularyAnswerCreate =
        SerializableFormularyAnswerCreate(
            value = domain.value,
            text = domain.text,
            timePeriod = domain.timePeriod,
            unit = domain.unit,
            uid = domain.uid,
            formId = domain.formId,
            groupName = domain.groupName,
            questionId = domain.questionId,
        )

    fun toDomain(serializable: SerializableQuestionDependency): QuestionDependency =
        QuestionDependency(
            idDependantQuestion = serializable.idDependantQuestion,
            idRequiredQuestion = serializable.idRequiredQuestion,
            dependencyExpression = serializable.dependencyExpression,
        )

    @OptIn(ExperimentalUuidApi::class)
    fun toDomain(serialized: SerializableCollectiveAction): CollectiveAction =
        CollectiveAction(
            id = serialized.id,
            images =
                serialized.images?.map { uri ->
                    Uri.parse(uri)
                } ?: emptyList(),
            name = serialized.name,
            description = serialized.description,
            authorId = Uuid.parse(serialized.metadata.id),
            authorName = serialized.metadata.name,
            startDate = serialized.startDate,
            endDate = serialized.endDate,
            status = serialized.status,
            members = serialized.members.map { toDomain(it.profile) },
        )

    fun toSerializableCreate(domain: CollectiveAction): SerializableCollectiveActionCreate =
        SerializableCollectiveActionCreate(
            name = domain.name,
            description = domain.description,
            startDate = domain.startDate,
            endDate = domain.endDate,
            status = domain.status,
        )

    fun toSerializableUpdate(domain: CollectiveAction): SerializableCollectiveActionUpdate {
        if (domain.id == null) {
            throw IllegalArgumentException("CollectiveAction id cannot be null or blank")
        }
        return SerializableCollectiveActionUpdate(
            id = domain.id,
            name = domain.name,
            description = domain.description,
            startDate = domain.startDate,
            endDate = domain.endDate,
            status = domain.status,
        )
    }

    @OptIn(ExperimentalUuidApi::class)
    fun toDomain(serialized: SerializableInvitation): Invitation =
        Invitation(
            id = serialized.id,
            invitedUser = UserProfile(Uuid.parse(serialized.invitedUser.id), serialized.invitedUser.name),
            actionId = serialized.action.id,
            actionName = serialized.action.name,
            accepted = serialized.accepted,
        )

    fun toDomain(serialized: SerializableMemberActivity): MemberActivity {
        val convertedEnum = enumValues<ActivityType>().find { it.type == serialized.activityType }
        if (convertedEnum == null) {
            throw IllegalArgumentException("Invalid ActivityType")
        }
        return MemberActivity(
            id = serialized.id,
            memberProfile = toDomain(serialized.member),
            actionId = serialized.actionId,
            type = convertedEnum,
            comment = serialized.comment,
            date = serialized.date.toLocalDateTime(TimeZone.currentSystemDefault()),
        )
    }

    fun toSerializableCreate(domain: MemberActivityCreate): SerializableMemberActivityCreate =
        SerializableMemberActivityCreate(
            actionId = domain.actionId,
            memberId = domain.memberId,
            activityType = domain.type.type,
            comment = domain.comment,
        )

    @OptIn(ExperimentalUuidApi::class)
    fun toDomain(serialized: SerializableUserProfile): UserProfile =
        UserProfile(
            id = Uuid.parse(serialized.id),
            fullName = serialized.name,
            profilePicture = serialized.profilePicture,
        )

    fun toSerializable(domain: Routine): SerializableRoutine {
        return SerializableRoutine(
            id = domain.id,
            userId = domain.userId,
            taskList = domain.taskList.map { toSerializable(it) }
        )
    }
    fun toDomain(serialized: SerializableRoutine): Routine {
        return Routine(
            id = serialized.id,
            userId = serialized.userId,
            taskList = serialized.taskList.map { toDomain(it) }
        )
    }
    fun toDomain(serialized: SerializableRoutineTask): RoutineTask {
        return RoutineTask(
            id = serialized.id,
            metaDataId = serialized.metaDataId,
            name = serialized.name,
            description = serialized.description,
            area = serialized.area,
            complete = serialized.complete,
            dueDate = LocalDate.parse(serialized.dueDate.toString()) // Converte String para LocalDate
        )
    }
    fun toSerializable(domain: RoutineTaskMetaData): SerializableRoutineTaskMetaData {
        return SerializableRoutineTaskMetaData(
            id = domain.id,
            routineId = domain.routineId,
            name = domain.name,
            description = domain.description,
            area = domain.area,
            weekdays = domain.weekdays
        )
    }
    fun toDomain(serialized: SerializableRoutineTaskMetaData): RoutineTaskMetaData {
        return RoutineTaskMetaData(
            id = serialized.id,
            routineId = serialized.routineId,
            name = serialized.name,
            description = serialized.description,
            area = serialized.area,
            weekdays = serialized.weekdays
        )
    }
}

package io.github.sustainow.repository.mapper

import android.net.Uri
import io.github.sustainow.domain.model.CollectiveAction
import io.github.sustainow.domain.model.Formulary
import io.github.sustainow.domain.model.FormularyAnswer
import io.github.sustainow.domain.model.Question
import io.github.sustainow.domain.model.QuestionAlternative
import io.github.sustainow.repository.model.SerializableCollectiveAction
import io.github.sustainow.repository.model.SerializableCollectiveActionCreate
import io.github.sustainow.repository.model.SerializableCollectiveActionUpdate
import io.github.sustainow.repository.model.SerializableFormulary
import io.github.sustainow.repository.model.SerializableFormularyAnswer
import io.github.sustainow.repository.model.SerializableQuestion
import io.github.sustainow.repository.model.SerializableQuestionAlternative
import io.github.sustainow.repository.model.SerializableQuestionDependency
import io.github.sustainow.repository.model.SerializableUserMetadata

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
        )
    }

    fun toDomain(serializable: SerializableQuestionDependency): Pair<Int, String> {
        return Pair(serializable.idDependantQuestion, serializable.dependencyExpression)
    }

    fun toSerializable(domain: Pair<Int, String>): SerializableQuestionDependency {
        return SerializableQuestionDependency(
            idDependantQuestion = domain.first,
            dependencyExpression = domain.second,
        )
    }

    fun toDomain(serialized: SerializableCollectiveAction): CollectiveAction {
        return CollectiveAction(
            id = serialized.id,
            images = serialized.images?.map {
                uri -> Uri.parse(uri)
            } ?: emptyList(),
            name = serialized.name,
            description = serialized.description,
            authorId = serialized.metadata.authorId,
            authorName = serialized.metadata.authorName,
            startDate = serialized.startDate,
            endDate = serialized.endDate,
            status = serialized.status
        )
    }

    fun toSerializable(domain: CollectiveAction): SerializableCollectiveAction {
        return SerializableCollectiveAction(
            id = domain.id,
            images = domain.images.map {
                uri-> uri.toString()
            },
            name = domain.name,
            description = domain.description,
            metadata = SerializableUserMetadata(authorId=domain.authorId,authorName = domain.authorName),
            startDate = domain.startDate,
            endDate = domain.endDate,
            status = domain.status
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
}

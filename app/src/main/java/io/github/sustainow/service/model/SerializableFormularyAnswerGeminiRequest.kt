import io.github.sustainow.repository.model.SerializableFormularyAnswer
import io.github.sustainow.service.model.SerializableTotalConsumptionGenerationConfig
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

@Serializable
class SerializableFormularyAnswerGeminiRequest(
    @SerialName("contents")
    val answersList: FormularyAnswerList,
    val generationConfig: SerializableTotalConsumptionGenerationConfig
)

@Serializable(with = FormularyAnswerListSerializer::class)
class FormularyAnswerList(val answers: List<SerializableFormularyAnswer>)

object FormularyAnswerListSerializer : KSerializer<FormularyAnswerList> {
    override val descriptor = buildClassSerialDescriptor("contents") {
            element("parts", buildClassSerialDescriptor("parts"))
        }
    override fun serialize(encoder: Encoder, value: FormularyAnswerList) {
        val composite = encoder.beginStructure(descriptor)

        // Create the outer array that will contain all answers
        val contentsArray = buildJsonArray {
            value.answers.forEach { answer ->
                        // Create the inner object with "text" field
                        add(buildJsonObject {
                            put("text", buildString {
                                append("Value: ${answer.value} ")
                                append("Unit: ${answer.unit}")
                                answer.timePeriod?.let {
                                    append(" Time Period: $it")
                                }
                            })
                        })
                    }
            }

        composite.encodeSerializableElement(
            descriptor,
            0,
            JsonElement.serializer(),
            contentsArray
        )

        composite.endStructure(descriptor)
    }

    override fun deserialize(decoder: Decoder): FormularyAnswerList {
        TODO("Not yet implemented")
    }

}

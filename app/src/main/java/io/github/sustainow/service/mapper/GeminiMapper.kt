package io.github.sustainow.service.mapper

import io.github.sustainow.domain.model.TotalConsumption
import io.github.sustainow.service.model.SerializableTotalConsumption

class GeminiMapper {
    fun toSerializable(domain: TotalConsumption): SerializableTotalConsumption  {
        return SerializableTotalConsumption(
            domain.total,
            domain.unit,
        )
    }

    fun toDomain(serializable: SerializableTotalConsumption): TotalConsumption  {
        return TotalConsumption(
            serializable.total,
            serializable.unit,
        )
    }
}

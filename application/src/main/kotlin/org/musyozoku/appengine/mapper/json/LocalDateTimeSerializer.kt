package org.musyozoku.appengine.mapper.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDateTime
import java.time.ZoneId

class LocalDateTimeSerializer : JsonSerializer<LocalDateTime>() {

    private val serializer = ZonedDateTimeSerializer()

    override fun serialize(value: LocalDateTime, gen: JsonGenerator, serializers: SerializerProvider) {
        serializer.serialize(value.atZone(ZoneId.systemDefault()), gen, serializers)
    }
}
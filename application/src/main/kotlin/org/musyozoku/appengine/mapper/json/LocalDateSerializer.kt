package org.musyozoku.appengine.mapper.json

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.JsonSerializer
import com.fasterxml.jackson.databind.SerializerProvider
import java.time.LocalDate
import java.time.ZoneId

class LocalDateSerializer : JsonSerializer<LocalDate>() {

    private val serializer = ZonedDateTimeSerializer()

    override fun serialize(value: LocalDate, gen: JsonGenerator, serializers: SerializerProvider) {
        val dateTime = value.atStartOfDay(ZoneId.systemDefault())
        serializer.serialize(dateTime, gen, serializers)
    }
}
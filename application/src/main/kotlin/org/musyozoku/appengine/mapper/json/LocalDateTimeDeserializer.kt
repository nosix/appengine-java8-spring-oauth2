package org.musyozoku.appengine.mapper.json

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import java.time.LocalDateTime

class LocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {

    private val deserializer = ZonedDateTimeDeserializer()

    override fun deserialize(p: JsonParser, ctx: DeserializationContext): LocalDateTime {
        return deserializer.deserialize(p, ctx).toLocalDateTime()
    }
}
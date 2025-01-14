package com.thesis.arrivo.communication.gson

import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class LocalDateTimeAdapter : TypeAdapter<LocalDateTime>() {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME

    override fun write(out: JsonWriter, value: LocalDateTime?) {
        out.value(value?.format(formatter))
    }

    override fun read(input: JsonReader): LocalDateTime? {
        return when (input.peek()) {
            JsonToken.NULL -> {
                input.nextNull()
                null
            }
            JsonToken.STRING -> {
                val dateString = input.nextString()
                LocalDateTime.parse(dateString, formatter)
            }
            else -> throw JsonSyntaxException("Expected a string for LocalDateTime but was ${input.peek()}")
        }
    }
}


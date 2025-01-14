package com.thesis.arrivo.communication.gson

import com.google.gson.JsonSyntaxException
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class LocalDateAdapter : TypeAdapter<LocalDate>() {
    private val formatter = DateTimeFormatter.ISO_LOCAL_DATE

    override fun write(out: JsonWriter, value: LocalDate?) {
        out.value(value?.format(formatter))
    }

    override fun read(input: JsonReader): LocalDate? {
        return when (input.peek()) {
            JsonToken.NULL -> {
                input.nextNull()
                null
            }

            JsonToken.STRING -> {
                val dateString = input.nextString()
                LocalDate.parse(dateString, formatter)
            }

            else -> throw JsonSyntaxException("Expected a string for LocalDate but was ${input.peek()}")
        }
    }
}


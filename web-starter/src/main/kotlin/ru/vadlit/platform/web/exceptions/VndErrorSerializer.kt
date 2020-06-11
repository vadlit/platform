package ru.vadlit.platform.web.exceptions

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import ru.vadlit.platform.web.exceptions.VndErrors.VndError

class VndErrorSerializer @JvmOverloads constructor(
    clazz: Class<VndErrors?>? = null
) : StdSerializer<VndErrors?>(clazz) {

    override fun serialize(
        value: VndErrors?,
        generator: JsonGenerator,
        provider: SerializerProvider
    ) {
        val errors: List<VndError> = value!!.toList()
        generator.writeStartObject()
        generator.writeNumberField("total", errors.size)
        generator.writeFieldName("_embedded")
        generator.writeStartObject()
        generator.writeObjectField("errors", errors)
        generator.writeEndObject()
        generator.writeEndObject()
    }
}
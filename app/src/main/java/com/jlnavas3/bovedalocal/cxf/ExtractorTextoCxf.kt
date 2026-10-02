package com.jlnavas3.bovedalocal.cxf

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

/**
 * Función de utilidad para extraer texto de un [JsonElement] que puede ser una primitiva (string o número)
 * o un objeto descriptor de campo (ej. {"fieldType": "string", "value": "..."} como en Google Password Manager).
 */
fun JsonElement?.extraerTexto(): String? {
    if (this == null) return null
    return when (this) {
        is JsonPrimitive -> if (this.isString) this.content else this.contentOrNull
        is JsonObject -> this["value"]?.jsonPrimitive?.contentOrNull ?: this["text"]?.jsonPrimitive?.contentOrNull
        else -> null
    }
}

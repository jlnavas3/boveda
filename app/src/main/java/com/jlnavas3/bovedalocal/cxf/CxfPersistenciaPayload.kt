package com.jlnavas3.bovedalocal.cxf

import android.content.Context
import java.io.File

/**
 * Persistencia en almacenamiento privado para datos temporales de la transferencia CXF.
 * Evita la pérdida de estado si el sistema suspende o recrea el proceso de la aplicación
 * mientras el gestor receptor (Google Password Manager, Dashlane) muestra su diálogo biométrico.
 */
object CxfPersistenciaPayload {

    private const val ARCHIVO_PAYLOAD = "cxf_export_payload.json"
    private const val ARCHIVO_SELECCIONADOS = "cxf_export_ids.txt"

    fun guardarPayload(context: Context, json: String) {
        try {
            val file = File(context.filesDir, ARCHIVO_PAYLOAD)
            file.writeText(json, Charsets.UTF_8)
        } catch (_: Exception) {}
    }

    fun recuperarPayload(context: Context): String? {
        return try {
            val file = File(context.filesDir, ARCHIVO_PAYLOAD)
            if (file.exists() && file.length() > 0) file.readText(Charsets.UTF_8) else null
        } catch (_: Exception) {
            null
        }
    }

    fun guardarIdsSeleccionados(context: Context, ids: Set<String>) {
        try {
            val file = File(context.filesDir, ARCHIVO_SELECCIONADOS)
            file.writeText(ids.joinToString("\n"), Charsets.UTF_8)
        } catch (_: Exception) {}
    }

    fun recuperarIdsSeleccionados(context: Context): Set<String>? {
        return try {
            val file = File(context.filesDir, ARCHIVO_SELECCIONADOS)
            if (file.exists() && file.length() > 0) {
                file.readLines(Charsets.UTF_8).filter { it.isNotBlank() }.toSet()
            } else null
        } catch (_: Exception) {
            null
        }
    }

    private const val ARCHIVO_ES_PERSONALIZADA = "cxf_es_personalizada.txt"

    fun guardarEsSeleccionPersonalizada(context: Context, esPersonalizada: Boolean) {
        try {
            val file = File(context.filesDir, ARCHIVO_ES_PERSONALIZADA)
            file.writeText(esPersonalizada.toString(), Charsets.UTF_8)
        } catch (_: Exception) {}
    }

    fun esSeleccionPersonalizada(context: Context): Boolean {
        return try {
            val file = File(context.filesDir, ARCHIVO_ES_PERSONALIZADA)
            if (file.exists()) file.readText(Charsets.UTF_8).trim().toBoolean() else false
        } catch (_: Exception) {
            false
        }
    }

    fun limpiar(context: Context) {
        try {
            File(context.filesDir, ARCHIVO_PAYLOAD).delete()
            File(context.filesDir, ARCHIVO_SELECCIONADOS).delete()
            File(context.filesDir, ARCHIVO_ES_PERSONALIZADA).delete()
        } catch (_: Exception) {}
    }
}

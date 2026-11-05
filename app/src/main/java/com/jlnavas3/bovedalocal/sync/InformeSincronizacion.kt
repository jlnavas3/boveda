/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.sync

/**
 * Clasificación de la transición de ciclo de vida de una entrada durante la sincronización.
 */
enum class TipoCambioSync {
    NUEVA,
    ACTUALIZADA,
    ENVIADA_A_PAPELERA,
    RESTAURADA_DE_PAPELERA
}

/**
 * Detalle individual de una entrada afectada por la sincronización.
 */
data class ItemCambioSync(
    val id: String,
    val titulo: String,
    val tipoCambio: TipoCambioSync,
    val mascara: MascaraCampos
)

/**
 * Informe determinista y estructurado del resultado de una sesión de sincronización P2P.
 * Emite en un único pase el balance exacto de cambios sin exponer credenciales sensibles.
 */
data class InformeSincronizacion(
    val nuevas: List<ItemCambioSync> = emptyList(),
    val actualizadas: List<ItemCambioSync> = emptyList(),
    val papelera: List<ItemCambioSync> = emptyList(),
    val sinCambiosCount: Int = 0,
    val dispositivoRemoto: String? = null
) {
    val totalModificadas: Int get() = nuevas.size + actualizadas.size + papelera.size
    val hayCambios: Boolean get() = totalModificadas > 0

    /**
     * Construye un resumen seguro y conciso de una línea para el Registro de Eventos y Diagnósticos.
     */
    fun textoResumen(): String {
        val prefijo = if (!dispositivoRemoto.isNullOrBlank()) "con $dispositivoRemoto: " else ""
        if (!hayCambios) return "${prefijo}Bóvedas idénticas (sin cambios, $sinCambiosCount al día)"

        val partes = mutableListOf<String>()
        if (nuevas.isNotEmpty()) {
            val titulos = nuevas.take(3).joinToString(", ") { "«${it.titulo}»" }
            val mas = if (nuevas.size > 3) " y ${nuevas.size - 3} más" else ""
            partes.add("${nuevas.size} nueva(s) ($titulos$mas)")
        }
        if (actualizadas.isNotEmpty()) {
            val titulos = actualizadas.take(3).joinToString(", ") { "«${it.titulo}»" }
            val mas = if (actualizadas.size > 3) " y ${actualizadas.size - 3} más" else ""
            partes.add("${actualizadas.size} actualizada(s) ($titulos$mas)")
        }
        if (papelera.isNotEmpty()) {
            val titulos = papelera.take(3).joinToString(", ") { "«${it.titulo}»" }
            val mas = if (papelera.size > 3) " y ${papelera.size - 3} más" else ""
            partes.add("${papelera.size} en papelera ($titulos$mas)")
        }
        if (sinCambiosCount > 0) {
            partes.add("$sinCambiosCount sin cambios")
        }
        return prefijo + partes.joinToString("; ")
    }
}

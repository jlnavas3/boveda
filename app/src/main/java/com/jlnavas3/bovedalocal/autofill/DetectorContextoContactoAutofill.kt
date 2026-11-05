/*
 * Copyright (C) 2026 Jose Luis Navas
 * Licensed under the GNU General Public License v3.0 (GPLv3).
 * See LICENSE file in the project root for full license information.
 */

package com.jlnavas3.bovedalocal.autofill

import android.app.assist.AssistStructure

/**
 * Resultado de la inspección contextual del formulario para detectar creación de contactos.
 */
data class ContextoContactoAutofill(
    val esContacto: Boolean,
    val nombreInicial: String? = null,
    val telefonoInicial: String? = null
)

/**
 * Microcomponente especializado que determina si una solicitud de autocompletado proviene
 * de una aplicación de mensajería o agenda donde el usuario está registrando un contacto nuevo,
 * distinguiéndolo con precisión quirúrgica de formularios de contraseña o páginas web.
 */
object DetectorContextoContactoAutofill {

    val PAQUETES_COMUNICACION = setOf(
        "com.whatsapp",
        "com.whatsapp.w4b",
        "org.telegram.messenger",
        "org.telegram.messenger.web",
        "org.telegram.plus",
        "nekox.messenger",
        "org.thoughtcrime.securesms",
        "com.viber.voip",
        "com.google.android.contacts",
        "com.google.android.dialer",
        "com.android.contacts",
        "com.android.dialer",
        "com.samsung.android.app.contacts",
        "com.samsung.android.dialer",
        "com.huawei.contacts",
        "com.hihonor.contacts",
        "com.oneplus.contacts"
    )

    fun esAppComunicacion(paquete: String, paquetesConfigurados: Set<String> = emptySet()): Boolean {
        val paquetes = paquetesConfigurados.ifEmpty { PAQUETES_COMUNICACION }
        return paquetes.any {
            paquete.equals(it, ignoreCase = true) || paquete.startsWith("$it.")
        }
    }

    fun esCandidatoContacto(
        paquete: String,
        tieneContrasena: Boolean,
        dominioWeb: String?,
        tieneCampoRelevante: Boolean,
        paquetesConfigurados: Set<String> = emptySet()
    ): Boolean {
        if (tieneContrasena) return false
        if (!dominioWeb.isNullOrBlank()) return false
        if (!esAppComunicacion(paquete, paquetesConfigurados)) return false
        return tieneCampoRelevante
    }

    fun detectar(
        paquete: String,
        campos: CamposDetectados,
        estructura: AssistStructure,
        paquetesComunicacion: Set<String> = emptySet(),
        pistasTelefono: List<String> = emptyList(),
        pistasNombre: List<String> = emptyList()
    ): ContextoContactoAutofill {
        val tieneCampoRelevante = campos.direccion.telefono != null ||
            campos.direccion.nombreCompleto != null ||
            campos.usuario != null

        if (!esCandidatoContacto(
                paquete = paquete,
                tieneContrasena = campos.contrasena != null,
                dominioWeb = campos.dominioWeb,
                tieneCampoRelevante = tieneCampoRelevante,
                paquetesConfigurados = paquetesComunicacion
            )
        ) {
            return ContextoContactoAutofill(esContacto = false)
        }

        // Inspeccionar el árbol de vistas para extraer texto pre-escrito en nombre o teléfono
        var nombreEncontrado: String? = null
        var telefonoEncontrado: String? = null
        val telKeywords = pistasTelefono.ifEmpty { com.jlnavas3.bovedalocal.data.AjustesDefaults.Autocompletado.PISTAS_CONTACTO_TELEFONO }
        val nomKeywords = pistasNombre.ifEmpty { com.jlnavas3.bovedalocal.data.AjustesDefaults.Autocompletado.PISTAS_CONTACTO_NOMBRE }

        fun recorrer(nodo: AssistStructure.ViewNode) {
            val id = nodo.autofillId
            val texto = nodo.text?.toString()?.trim()
            if (!texto.isNullOrBlank()) {
                val pistas = buildList {
                    nodo.autofillHints?.forEach { add(it.lowercase()) }
                    nodo.hint?.lowercase()?.let { add(it) }
                    nodo.idEntry?.lowercase()?.let { add(it) }
                }
                val esTel = id == campos.direccion.telefono || pistas.any { p ->
                    telKeywords.any { p.contains(it) }
                }
                val esNom = id == campos.direccion.nombreCompleto || pistas.any { p ->
                    nomKeywords.any { p.contains(it) }
                }

                if (esTel && telefonoEncontrado == null) {
                    telefonoEncontrado = texto
                } else if (esNom && nombreEncontrado == null) {
                    nombreEncontrado = texto
                }
            }

            for (i in 0 until nodo.childCount) {
                nodo.getChildAt(i)?.let { recorrer(it) }
            }
        }

        for (i in 0 until estructura.windowNodeCount) {
            estructura.getWindowNodeAt(i)?.rootViewNode?.let { recorrer(it) }
        }

        return ContextoContactoAutofill(
            esContacto = true,
            nombreInicial = nombreEncontrado,
            telefonoInicial = telefonoEncontrado
        )
    }
}
